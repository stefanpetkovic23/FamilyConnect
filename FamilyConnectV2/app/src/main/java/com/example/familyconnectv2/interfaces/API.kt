package com.example.familyconnectv2.interfaces

import com.example.familyconnectv2.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface API {

    @POST("/api/user")
    fun registerUser(@Body body: User): Call<userResponse>

    @POST("/api/user/login")
    fun login(@Body loginRequest: LoginRequest): Call<userResponse>

    @POST("/api/user/logout")
    fun logoutUser(): Call<Void>

    @POST("/api/todo/add-task")
    fun addTask(@Header("Authorization") token: String, @Body task: ToDoRequest): Call<ToDoResponse>

    @GET("/api/todo/get-all-tasks")
    fun getAllTasksForUser(@Header("Authorization") token: String): Call<List<ToDoTasksItem>>

    @GET("/api/user")
    fun getAllUsers(): Call<List<userResponse>>

    @GET("/api/user/getuseridbyemail/{email}")
    fun getUserIdByEmail(
        @Header("Authorization") authorization: String,
        @Path("email") email: String
    ): Call<UserIdResponse>

    @GET("/api/group/get-group-by-user/{userId}")
    fun getGroupsByUserId(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String
    ): Call<groupResponse>

    @POST("/api/group/create-group")
    fun createGroup(@Header("Authorization") authorization: String,@Body request: GroupCreationRequest): Call<Group>

    @GET("/api/group/get-todo-from-groups-by-user/{userId}")
    fun getTasksFromGroupsByUserId(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Call<TasksFromGroup>

    @POST("/api/group/add-task-to-group")
    fun addTaskToGroup(@Header("Authorization") authorization: String,@Body request: ToDoGroupRequest): Call<ToDoGroupResponse>

    @POST("/api/group/add-item-to-shoppinglist")
    fun addItemToShoppingListInGroup(
        @Header("Authorization") token: String,
        @Body request: ShoppingItemRequest
    ): Call<Group>

    @GET("/api/group/get-shopping-items/{userId}")
    fun getShoppingItemsForUser(@Header("Authorization") token: String, @Path("userId") userId: String): Call<List<ShoppingGroup>>

    @POST("/api/group/add-activity-to-group")
    fun addActivityToGroup(@Header("Authorization") token: String,@Body request: AddActivityToGroup): Call<AddActivityGroupResponse>

    @POST("/api/activity/add-activity")
    fun addActivity(@Header("Authorization") token: String,@Body request: ActivityDetails): Call<ActivityFull>

    @GET("/api/group/get-activities-for-date/{userId}")
    fun getActivitiesForDate(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Call<ActivitiesFromGroups>

    @PUT("/api/todo/update-task/{taskId}")
    fun updateTask(
        @Header("Authorization") token: String,
        @Path("taskId") taskId: String
    ): Call<Task>

    @PUT("/api/todo/update-privatetask/{taskId}")
    fun updatePrivateTask(
        @Header("Authorization") token: String,
        @Path("taskId") taskId: String
    ): Call<Task>

    @GET("/api/activity/all-activities/{userId}")
    fun getAllActivitiesByUser(@Header("Authorization") token: String, @Path("userId") userId: String): Call<List<ActivityFull>>

    @PUT("/api/shopping/update-item/{itemId}")
    fun updateShoppingItem(
        @Header("Authorization") token: String,
        @Path("itemId") itemId: String
    ): Call<ShoppingList>

    @GET("/api/chat/get-user-chat")
    fun fetchChats(@Header("Authorization") token: String): Call<List<Chat>>

    @GET("/api/message/{chatId}")
    fun getAllMessages(@Header("Authorization") token: String, @Path("chatId") chatId: String): Call<List<ChatMessageDetails>>

    @POST("/api/message/send-message")
    fun sendMessage(@Header("Authorization") token: String,@Body request: SendMessageRequest): Call<SendMessageResponse>

    @POST("api/chat/create-group-chat")
    fun createGroupChat(@Header("Authorization") token: String,@Body request: GroupCreationRequest): Call<CreateChatResponse>

    @GET("api/user/userdata/{email}")
    fun getUserData(@Header("Authorization") token: String,@Path("email") email: String): Call<UserInfo>

    @DELETE("/api/activity/delete-activity/{activityId}")
    fun deleteActivity(
        @Header("Authorization") token: String,
        @Path("activityId") activityId: String
    ): Call<CreateChatResponse>

    @DELETE("/api/todo/delete-task/{taskId}")
    fun deleteTask(
        @Header("Authorization") token: String,
        @Path("taskId") taskId: String
    ): Call<CreateChatResponse>

}