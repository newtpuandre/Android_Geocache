package main

import (
	"context"
	"fmt"
	"log"
	"net/http"
	"os"
	"time"

	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
	"github.com/rs/xid"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

const (
	dbURL       = "mongodb://test_user:test123@ds135726.mlab.com:35726/map_messages"
	searchRange = 10
)

var startTime time.Time
var client mongo.Client

type Message struct {
	messageID string  `json:"messageID"`
	timestamp string  `json:"timestamp"`
	userID    string  `json:"userID"`
	message   string  `json:"message"`
	imageURL  string  `json:"imageURL"`
	long      float32 `json:"long"`
	lat       float32 `json:"lat"`
	mType     int     `json:"mType"` //private/friend/public messages
}

type User struct {
	userID    string   `json:"userID"`
	userName  string   `json:"userName"`
	passHash  string   `json:"passHash"`
	friendIDs []string `json:"friendIDs"`
}

type MessageRequest struct {
	userID string  `json:"userID"`
	long   float32 `json:"long"`
	lat    float32 `json:"lat"`
}

func getMessages(c *gin.Context) {
	type position struct {
		long float32 `json:"long"`
		lat  float32 `json:"lat"`
	}

	var messageRequest MessageRequest
	c.BindJSON(&messageRequest)

	fmt.Println(messageRequest)

	/*mdb := client.Database("map_messages").Collection("Messages")

	mdb.Aggregate(
		{$unwind: "$Messages"},
		{$match: {"Messages.lat": {"$gte":messageRequest.lat-range, "$lte":messageRequest.lat+range}}},
		{$match: {"Messages.long": {"$gte":messageRequest.long-range, "$lte":messageRequest.long+range}}}
	)*/

	//dummy reply
	var messages [2]Message
	messages[0] = Message{
		"TestMessage1",
		time.Now().String(),
		"DummyUser",
		"This is the first test message it has an imageURL",
		"https://images.pexels.com/photos/2156311/pexels-photo-2156311.jpeg",
		messageRequest.long,
		messageRequest.lat,
		0}
	messages[1] = Message{
		"TestMessage2",
		time.Now().String(),
		"UserDummy",
		"This is the first test message it does NOT have an imageURL",
		"",
		messageRequest.long + 0.001,
		messageRequest.lat + 0.001,
		0}
	//end of dummy reply

	c.JSON(http.StatusOK, messages)
}

func postMessage(c *gin.Context) {
	var message Message
	c.BindJSON(&message)

	message.messageID = xid.New().String()
	message.timestamp = time.Now().String()

	_, err := client.Database("map_messages").Collection("Messages").InsertOne(context.TODO(), message)
	if err != nil {
		log.Fatal(err)
	}

	c.Status(http.StatusOK)
}

func getUserInfo(c *gin.Context) {
	type userID struct {
		userID string `json:"userID"`
	}

	var uID userID
	c.BindJSON(&uID)

	filter := bson.D{{"userID", uID.userID}}

	var result User
	err := client.Database("map_messages").Collection("Users").FindOne(context.TODO(), filter).Decode(&result)
	if err != nil {
		log.Fatal(err)
	}

	//return user
}

//User creation
func postUser(c *gin.Context) {
	var user User
	c.BindJSON(&user)

	user.userID = xid.New().String()

	_, err := client.Database("map_messages").Collection("Users").InsertOne(context.TODO(), user)
	if err != nil {
		log.Fatal(err)
	}

	c.Status(http.StatusOK)
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

func main() {

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
		api.GET("/message", getMessages)  //fetch messages should return collection of available messages to user
		api.POST("/message", postMessage) //post a new message to db
		api.GET("/user", getUserInfo)     //fetch user information
		api.POST("/user", postUser)       //create new user account
	}

	//Listens to environment declared port, using the subrouter handlers
	router.Run(port)
}
