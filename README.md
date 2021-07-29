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


Week 4 (7/12-7/16):

- Set up all Fragment and Activity layouts (Compose, Inbox, Main Wall, Profile fragments) and Sign Up, Login, Main Activities, MessageDetails Activity
- Finished all layouts as shown in Wireframe. Added all necessary onClickListeners.
- Set up Parse User login, logout, and sign-up with persistence.
- Add a new class in Back4App to represent a 'Message' that a user sends (Message.java) and implemented Message class get and set methods.
- Implemented all necessary fields for the User class
- Add RecyclerViews and corresponding adapters for Inbox and Main Wall fragments.
- Implemented being able to compose a message and send it to a valid user. Checked that it populates correctly in the Back4App database by saving a message with all fields in the database.
- Replaced EditView with AutocompleteView in compose fragment.
- Implemented AutoCompleteTextView which allows users to type two characters and then see users that they might be looking to select with a dropdown menu.
- Implemented feature that users are able to choose their own profile picture by taking a picture and setting it as their profile picture on the profile screen.
- Implemented feature to obtain all Messages sent to the current logged in ParseUser.
- Populated Inbox RecyclerView and MainWall RecyclerView with notes. Implemented Refresh screen (swipe down) to retrieve new messages.
- Implemented pinning messages to the user's main wall by updating Message class to include isPinned attribute. Updated inbox Messages to be saved in Parse Server.
- Finished changing the color of an inbox message pin when clicked. Changed color layout and icon format.
- Added error checking to handle when getActivity() is null before populating the autocomplete adapter. Also added finish() when logging out to ensure that user cannot return back to MainActivity
- Implemented Sentiment analysis on text user enters using https://rapidapi.com/.../api/text-sentiment-analysis-method/ API. Retrieves a compose message’s positive, negative, and neutral percentage.
- Updated inbox recyclerView with card layout. A message view can be swiped right to delete and Snackbar message gives the option to undo delete. Updated adapter and recyclerView.
- Updated profile picture to be circular shape and with border. Used circleimageview library.
- Updated profile fragment layout to display field for number of messages sent.
- Added Space tab Layout to UI. Allows user to swipe between fragments [Disables this in Week 5 since it interferes with the inbox delete message swiping]
- Request User's full name in sign up so that can be used to populate [user]'s Wall
- Updated profile photo with default add profile picture icon
- Added hey_magnolia font to project as the wall font
- Added using Toolbar as an action bar in order to display search icon and implement searching in Main Wall RecyclerView.
- Finished implementing search bar and filtering in Main wall.
- Updated toast messages to Snackbar so that they are better displayed when a user is using the keyboard
- Finished remaining Parse delete/get/put implementations for the Inbox and Main wall screen.
- Implemented deleting a message from Parse when it is swiped right in the user's inbox.
- Implemented an undo feature to not delete message if the user clicks undo on the Snackbar action.
- Fixed bug where initially wouldn't delete message from backend if user swiped another inbox message before the Snackbar timed out. Added DISMISS_EVENT_CONSECUTIVE so that a message will be deleted if a Snackbar was dismissed from a new Snackbar being shown. Also added other DISMISS_EVENTS to account for all edge cases.
- Updated Profile fragment with better profile image icon.
- Used https://rapidapi.com/bitbiscuit.../api/motivational-quotes1/ Motivational API to get and populate a motivational quote on the profile screen.
- Updated Inbox fragment to update the number of notes sent by and populate Profile fragment with value.
Week 5 (7/19-7/23):

- Implemented displaying to the user a dialog box if the Sentiment Analysis API returns that a message about to be sent is negative. Dialog box asks if they would still want to send a message if negative percentage is greater than 50%
- Added second sentiment analysis API. Added DialogBox to warn users that their message might be negative. Prompts them to decide to continue sending or don't send.
- Implemented DoNotSendDialogFragment which extends DialogFragment.
- Defined the listener interface in DoNotSendDialogFragment
- Implemented sendBackResult() method in DoNotSendDialogFragment to send data back to ComposeFragment
- Had ComposeFragment implement the DoNotSendDialogListener
- Displayed the dialog within ComposeFragment and setTargetFragment as ComposeFragment
- Called prepareMessage() within the onFinishDoNotSendDialog if toSend(user chooses to send in dialog box) is true, else display message not sent!
- Fixed bug where if neutral/positive sentiment was detected then message didn't send (message should send by default if no negative sentiment detected)
- Fixed issue with profile picture not persisting when switching to the Inbox and Main wall tab. Now saving file in background first before putting file in User's profile picture field and then saving user in background.
- Added Perspective API which shows toxicity value of a message.
- Disabled swiping in viewpager (by creating a custom viewpager) in order to use swiping to delete a message from inbox.
- Updated profile picture click to prompt the user if they want to take a photo or choose a photo from their gallery. Added SelectCameraFragment dialogFragment to do this. Also needed to add a verifyStoragePermissions method to ask the user for permission to access their gallery if it isn't already allowed.
- Updated isUnread boolean as being false if a user clicks on a message in their inbox. Visually grays out that message when the screen is refreshed.
- Added animations to login screen (https://github.com/dynamitechetan/VusikView) and details screen(https://github.com/glomadrian/Grav)
- Added dropdown menu to MainWallFragment which allows users to decide which wall of sticky notes they want to see with onOptionsItemSelected filtering the messages based on the option/wall selected by creating helper filter methods for each wall. Updated adapter with the filtered notes per wall.
- Finished creating dialog fragment called from Inbox Fragment to let a user decide which wall they want to pin a note to (memories, kudos, or goals) which updates back4app accordingly (added three boolean columns for each wall).
- Added water droplet swipe down and refresh
- Made Sticky notes on each wall different colors. Main wall background added.
- Added Interface to the MainWallFragmentAdapter in order to pass information from the MainWallFragment to the bind function in the adapter in order to change the sticky note colors based on the wall being displayed.
- Updated backgrounds for each screen.
- Updated MainWallFragment so that when a wall is selected and then search is used then we search the sticky notes within that specific wall.
- Implemented autofilling a random compliment if a user decides to press the random compliment button.

Week 6 (7/26-today):

- Added a send message icon on the ComposeMessage screen where if a user want to send a message to a user that's not on the app they have the option to still compose the message and then click the send text message icon which will allow the user to share it via email/text/other apps. Added a layout for setting up a sticky note graphic where the message the user composes is the text within the sticky note along with a message on top where it says that 'you were invited by [user] to join the app!' (used buildDrawingCache and creating a bitmap) and then created a ACTION_SEND intent)
- MainWall: Added a onOptionsItemSelected case for when the search icon is pressed so that it doesn't automatically refresh to showing all the messages (adapter filter set to only use messages of the current wall).
- Added scrolling to the details sticky note so that if the user receives a really long message that doesn't fit on a sticky note they can still scroll through the text on the sticky note.
- Updated sticky note graphics for the case when you initially load the main wall.
- Updated inbox screen so that it shows a graphic first that says Loading messages... followed by how many unread messages there are.
- Added ‘add friend’ and ‘add group’ buttons on profile screen.
- Set up FriendRequest class in Parse and added class in project.
- Finished complete functionality of sending friend requests, accepting friend requests, rejecting friend requests, and unfriending. (Pending/Friended/Unfriended/Rejected).
- Finished Undo option on Inbox screen where a user can click Undo multiple times to undo delete previously deleted messages (compared to the Snackbar undo delete option which only lets you undo delete the most recently deleted message). Once a user refreshes the screen, the messages swiped that weren’t undo-ed are deleted permanently.
- Finished implementing Facebook SDK login where a user is automatically signed up with their name as their username if a first-time user and otherwise logged in automatically (after entering their fb login info) if they already have an account.

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
  
