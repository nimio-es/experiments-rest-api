package io.theam.client.service

import io.theam.model.api.UserData

class UsersRestClient(username: String, password: String) : BaseRestClient(username, password)  {

    companion object {
        private const val base_users_url = base_url + "/users"
    }

    // ----

    val users: Collection<UserData>
        get() {
            val getUrl = base_users_url;
            val users = restTemplate.getForEntity(
                    getUrl,
                    Collection::class.java as Class<Collection<UserData>>)

            return users.body
        }

    fun createNewUser(username: String, password: String, isAdmin: Boolean) : UserData {
        val postUrl = "$base_users_url?username=$username&password=$password" + (if(isAdmin) "&asAdmin=true" else "")
        return restTemplate.postForObject(postUrl, null, UserData::class.java)
    }
}