Original App Design Project - README Template
===

# Cup of Positivitea

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
The overall concept of the app is that users are able to send each other kind messages, wishes, and memories which is by default shared anonymously unless the user wants to share their name. When a message is sent it arrives to the recepient 'in the mail' and the recipient clicks on their mailbox icon in order to view messages that have been sent to them. The recipient can decide which messages to accept or reject and can categorize them and post them across different personal 'walls' which are labelled by the user's preference. The message they recieve comes on a sticky note and that sticky note is then tacked on the wall they select giving each user a wall of notes they can come back to anytime for encouragement! Each sticky note can be clicked on to view the entire message. Future goals include making it easy to use this app as a wholesome group activity where each member of a group is assigned another individual at random to send a kind note too. Another goal is to incorporate kindness 'hearts' into the app where for every compliment you send you get a heart which can be potentially used to upgrade features. Additionally, another goal is to get permission to access a user's contact list and randomly select and provide to the user if they would like to send them a note! If recipient user is not on the app might have a way to connect to the user's texting app and creating the sticky note template note so that they are still able to send the note along with a link to the app!

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Wellness/Social
- **Mobile:** Can connect with user's text service and app makes it easy to view and send ntoes.
- **Story:** Users can easily send appreciation notes to others anonymously and also recieve notes that they can keep to view whenever they would like a pick-me-up!:)
- **Market:** Anyone!
- **Habit:** Daily incentives to send notes! With group feature will also increase the usage of the app.
- **Scope:** Global.

## Product Spec

#1 Technical challenges for your project (Some features that you have not learned from code path).

#2 Set the reasonable milestones for your milestones. Please set the milestones for your required feature first (don't think about the stretch goal), also include the work for debugging and polishing.

Milestones:

Week 4
- Finish all layouts as shown in Wireframe. Add OnClickListeners to go from One Activity to the next.
- Use Parse and Back4App for User login, logout, and sign-up.
- Add a new class in Back4App to represent a 'Message' that a user sends (Message.java)
- For the Message object, add a sender and reciever field which will hold a pointer to the corresponding User. Research this part more on how we can reference the current user and how we can select from the list of other users. 
- Set up composing a message and saving message with all fields in database.
- Populate new messages a user has recieved in their main_wall_notes field.
- Stretch: Create a dropdown menu which has a recyclerView that can be scrolled to display all the other users and the User can search for the recipient they would like to send a message to.
- Stretch: Be able to select profile picture.
- Stretch: Populate Inbox RecyclerView and MainWall RecyclerView with notes. Refresh screen (swipe down) to retrieve new messages.
- Stretch: Implement remaining Parse API tasks as outlined in the Network section (deleting a message from inbox and wall).

Week 5
- Finish basic messaging system (send and recieve) and test using Log statements. 
- Implement necessary RecyclerView/Adapters in the Inbox activity to view the messages. The RecyclerView will contain ViewHolders that have a TextView (for the message), Date Sent (TextView), think about other important fields to include.
- Update Back4App with Message fields to Delete/Read/Unread. 
- Implement RecyclerView (Grid Layout) for the MainWall Activity. Will need the ViewHolder to contain an ImageView within which is a TextView whose text is set with the messages in the inbox. For now, I will test with hardcoded examples. Each ViewHolder will also contain a button (pin).
- Implement OnClickListener for pinning a message in the Inbox. When the pin is clicked, the text in the TextView (from the Inbox row) should be sent to the MainWall Activity via an intent and should be populated in the RecyclerView at the top (most recent messages at the top of the wall). Implement all the elements of binding in the adapter and notifying the RecyclerView.

Week 6
- Right now all messages are populated to one wall. Implement feature to allow users to decide which wall(Activity) they want to pin a message to. Allow users the option to potentially select the color of the sticky notes?
- Add Sentiment Analysis API which will be used on a Message text the user is about to send and will prompt the user to rethink their message if the sentiment is not nice.
- Allow users to add Sticky Notes of their own to their personal walls (Ex. My Memories, My goals) users should be able to populate these walls themselves, bypassing the send/recieve.

Week 7
-Explore other SDKs for real-time message (Some on Codepath site, also looking into ScaleDrone SDK. (Stretch Goal)

Week 8

Add in:
- Facebook user login API, make Sentiment Analysis API a stretch goal.
- Include a gesture (such as swiping between activities, zoom in/zoom out.
- Include an animation.
- Visual polish external library
- Technical Challenge: Sentiment Analysis, Store encrypted text in Server, decrypt message on Client end.

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* RecyclerView with Sticky note images that can have text embedded for User main personal wall.             
* Sending/Recieving messages feature and populating main user wall
* Animations/images for each screen 
* Login/logout feature

**Optional Nice-to-have Stories**

* Sentiment Analysis checker for all messages about to be sent (https://learn.meaningcloud.com/developer/sentiment-analysis/2.1/dev-tools)
* Supporting group feature
* Supporting external chat feature
* Choosing color of sticky note and background wall image. More ways to personalize. Maybe color of sticky note can be used to categorize?

### 2. Screen Archetypes

* Login Screen
   * [list associated required story here]
   * ...
* Home Screen
   * [list associated required story here]
   * ...
* Mailbox Screen
   * [list associated required story here]
   * ...
* Main Wall Screen
   * [list associated required story here]
   * ...
* See More Screen
   * [list associated required story here]
   * ...
* Optional Wall Screen
   * [list associated required story here]
   * ...
* Optional Wall Screen
   * [list associated required story here]
   * ...
* Optional Wall Screen
   * [list associated required story here]
   * ...
* Optional Wall Screen
   * [list associated required story here]
   * ...
* Optional Wall Screen
   * [list associated required story here]
   * ...


### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Login Screen
* Home Screen
* Mail Screen
* Main Wall Screen

**Flow Navigation** (Screen to Screen)

* Login Screen
   * Home Screen
   * ...
* Home Screen
   * Mail Screen
   * Main Wall Screen
* Mail Screen 
   * Home Screen
   * Main Wall Screen
* Main Wall Screen
   * Mail Screen
   * Home Screen
     

## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="wireframe.png" width=600>

### [BONUS] Digital Wireframes & Mockups
https://www.figma.com/proto/Jj54bymkKC4RQrTqj9PV7C/Untitled?node-id=2%3A13&scaling=scale-down&page-id=0%3A1

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models

Message
| Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user post (default field) |
   | sender        | Pointer to User| message sender |
   | reciever         | Pointer to User     | message reciever |
   | message_body       | String   | text message that sender is sending |
   | createdAt     | DateTime | date when post is created (default field) |
   | updatedAt     | DateTime | date when post is last updated (default field) |
   | createdAt     | DateTime | date when post is created (default field) |
   | unread     | Boolean | whether message has been read or not |
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]

 - Inbox Screen
      - (Read/GET) Query all posts where user is reciever

      ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
query.whereEqualTo("reciever", currentUser);
query.whereEqualTo("unread", true);
query.findInBackground(new FindCallback<ParseObject>() {
    public void done(List<ParseObject> scoreList, ParseException e) {
        if (e == null) {
            Log.d("message ", "Retrieved for " + currentUser);
        } else {
            Log.d("message ", "Error: " + e.getMessage());
        }
    }
});

  - Profile Screen 
        - (Read/GET) Query logged in user object
        - (Update/PUT) user profile image
        - (Update/PUT) Number of notes sent
        
  - Compose Screen
      - (Create/POST) Create a new message object

  - Inbox Screen
      - (Read/GET) Query messages where user is reciever
      - (Delete) Delete a message from database
      - (Update/PUT) Update message as read
  
  - Main Wall
      - (Create/POST) Add a new message to wall
      - (Delete) Delete a message from wall database
  
