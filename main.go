package main

import (
	"context"
	"fmt"
	"log"
	"net/http"
	"os"
	"strconv"
	"time"

	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
	"github.com/rs/xid"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
	"golang.org/x/crypto/bcrypt"
)

const (
	dbURL       = "mongodb://test_user:test123@ds135726.mlab.com:35726/map_messages"
	searchRange = 0.5
	pwdSalt = 23
)

var startTime time.Time
var client mongo.Client

type Message struct {
	MessageID string  `json:"messageID" bson:"_id"`
	Timestamp string  `json:"timestamp" bson:"timestamp"`
	UserID    string  `json:"userID" bson:"userid"`
	Message   string  `json:"message" bson:"message"`
	ImageURL  string  `json:"imageURL" bson:"imageurl"`
	Long      float32 `json:"long" bson:"long"`
	Lat       float32 `json:"lat" bson:"lat"`
	MType     int     `json:"mType" bson:"mtype"` //private/friend/public messages
}

type User struct {
	FullName string `json:"fullName" bson:"fullname"`
	UserID   string `json:"userID" bson:"_id"`
	UserName string `json:"userName"`
	PassHash string `json:"passHash"`
	//FriendIDs      []string `json:"friendIDs"`
	CachesFound    int `json:"cachesFound"`
	DistanceWalked int `json:"distanceWalked"`
}

type MessageRequest struct {
	UserID string  `json:"userID" bson:"_id"`
	Long   float32 `json:"long"`
	Lat    float32 `json:"lat"`
}

func getMessages(c *gin.Context) {
	client := returnClient()
	var messageRequest MessageRequest
	c.BindJSON(&messageRequest)

	var messages []Message
	mdb := client.Database("map_messages").Collection("Messages")

	//attempt to get query from db rather than fetching all/filtering in go
	/*b1 := bson.M{"$unwind": "$Messages"}
	b2 := bson.M{"$match": bson.M{"Messages.lat": bson.M{
		"$gt":messageRequest.Lat-searchRange,
		"$lt":messageRequest.Lat+searchRange}}}

	b3 := bson.M{"$match": bson.M{"Messages.long": bson.M{
		"$gt":messageRequest.Long-searchRange,
		"$lt":messageRequest.Long+searchRange}}}

	operations := []bson.M{b1,b2,b3}
	cursor,err := mdb.Aggregate(context.TODO(), operations)
	//cursor,err := mdb.Find(context.TODO(), operations)
	if err != nil {
		log.Fatal(err)
	}
	cursor.All(context.TODO(), &messages)*/
	//end of attempt

	filter := bson.M{}

	cursor, err := mdb.Find(context.TODO(), filter)
	if err != nil {
		log.Fatal(err)
	}

	cursor.All(context.TODO(), &messages)

	var returnMessages []Message

	for iter, message := range messages {

		//debug printing
		println("message No." + strconv.Itoa(iter))
		println(message.Lat)
		println(message.Long)

		if message.Lat >= messageRequest.Lat-searchRange && message.Lat <= messageRequest.Lat+searchRange &&
			message.Long >= messageRequest.Long-searchRange && message.Long <= messageRequest.Long+searchRange {
			returnMessages = append(returnMessages, message)
		}
	}

	c.JSON(http.StatusOK, returnMessages)
}

func postMessage(c *gin.Context) {
	client := returnClient()

	var message Message
	c.BindJSON(&message)

	message.MessageID = xid.New().String()
	message.Timestamp = time.Now().String()

	_, err := client.Database("map_messages").Collection("Messages").InsertOne(context.TODO(), message)
	if err != nil {
		log.Println(err)
		c.String(http.StatusBadRequest, "")
	}

	c.String(http.StatusOK, "\""+message.MessageID+"\"")
}

func getUserInfo(c *gin.Context) {
	client := returnClient()

	type userID struct {
		UserID string `json:"userID" bson:"_id"`
	}

	var uID userID
	c.BindJSON(&uID)

	filter := bson.M{"_id": uID.UserID}

	type retUser struct {
		FullName       string `json:"fullName"`
		UserID         string `json:"userID" bson:"_id"`
		UserName       string `json:"userName"`
		CachesFound    int    `json:"cachesFound"`
		DistanceWalked int    `json:"distanceWalked"`
	}
	var result retUser
	err := client.Database("map_messages").Collection("Users").FindOne(context.TODO(), filter).Decode(&result)
	if err != nil {
		log.Fatal(err)
	}

	//return user
	c.JSON(http.StatusOK, result)
}

func getUserInfoByName(c *gin.Context) {
	client := returnClient()

	type UserName struct {
		FullName string `json:"fullName" bson:"fullname"`
	}

	var name UserName
	c.BindJSON(&name)

	filter := bson.M{"fullname": name.FullName}

	type retUser struct {
		FullName       string `json:"fullName"`
		UserID         string `json:"userID" bson:"_id"`
		UserName       string `json:"userName"`
		CachesFound    int    `json:"cachesFound"`
		DistanceWalked int    `json:"distanceWalked"`
	}
	var result retUser
	err := client.Database("map_messages").Collection("Users").FindOne(context.TODO(), filter).Decode(&result)
	if err != nil {
		log.Fatal(err)
	}

	//return user
	c.JSON(http.StatusOK, result)
}

//User creation
func postUser(c *gin.Context) {
	client := returnClient()

	var user User
	c.BindJSON(&user)

	user.UserID = xid.New().String()
	filter := bson.M{"fullname": user.FullName}

	var foundUser User
	err := client.Database("map_messages").Collection("Users").FindOne(context.TODO(), filter).Decode(&foundUser)
	if err != nil {
		println(err.Error())
	}

	if foundUser.FullName == user.FullName {
		c.Status(http.StatusBadRequest)
	} else {
		user.PassHash = hashAndSaltPassword(user.PassHash)
		_, err := client.Database("map_messages").Collection("Users").InsertOne(context.TODO(), user)
		if err != nil {
			log.Fatal(err)
		}
		c.Status(http.StatusOK)
	}
}

func userLogin(c *gin.Context) {
	type LoginInfo struct {
		Fullname string `json:"fullName"`
		Password string `json:"password"`
	}
	var loginInfo LoginInfo
	c.BindJSON(&loginInfo)

	var user User
	filter := bson.M{"fullname": user.FullName}
	err := client.Database("map_messages").Collection("Users").FindOne(context.TODO(), filter).Decode(&user)
	if err != nil {
		println(err.Error())
	} else {
		err = bcrypt.CompareHashAndPassword([]byte(user.PassHash), []byte(loginInfo.Password))
		if err != nil {
			//wrong password
			c.Status(http.StatusUnauthorized)
		} else {
			c.JSON(http.StatusAccepted, user)
		}
	}
}

func hashAndSaltPassword(pwd string) string {
	hash, err := bcrypt.GenerateFromPassword([]byte(pwd), pwdSalt)
	if err != nil {
		log.Fatal(err)
	}
	return string(hash)
}

/*
determineListenAdress fetches the environment variable "PORT"
*/
func determineListenAddress() (string, error) {
	port := os.Getenv("PORT")
	if port == "" {
		return "", fmt.Errorf("$PORT not set")
	}
	return ":" + port, nil
}

func init() {
	startTime = time.Now()
	fmt.Printf("Program Starting\nCurrent time: %v\n\n", startTime.Format(time.UnixDate))
}

func returnClient() *mongo.Client {
	client, err := mongo.NewClient(options.Client().ApplyURI(dbURL))
	if err != nil {
		log.Fatal(err)
	}
	ctx, cancel := context.WithTimeout(context.Background(), 20*time.Second)
	defer cancel()

	err = client.Connect(ctx)
	if err != nil {
		log.Fatal(err)
	}
	return client
}

func main() {

	port, err := determineListenAddress()
	if err != nil {
		fmt.Printf("Unable to determine port from environment variable. Setting port to 8080 by default!\nError Message: %v\n\n", err.Error())
		port = ":8080"
	}

	//Create new router, and subrouter with the root prefix
	router := gin.Default()
	router.Use(cors.Default())

	api := router.Group("/api")
	{
		api.POST("/getmessages", getMessages) //fetch messages should return collection of available messages to user
		api.POST("/message", postMessage)     //post a new message to db
		api.GET("/user", getUserInfo)         //fetch user information
		api.POST("/user", postUser)           //create new user account
		api.POST("/login", userLogin)         //log in user
	}

	//Listens to environment declared port, using the subrouter handlers
	router.Run(port)
}
