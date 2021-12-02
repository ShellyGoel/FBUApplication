Original App Design Project - README Template
===

# Cup of Positivitea

## Table of Contents
1. [Overview](#Overview)
2. [Schema](#Schema)

## Overview
### Description
The overall concept of the app is that users are able to send each other kind messages, wishes, and memories which is by default shared anonymously unless the user wants to share their name. When a message is sent it arrives to the recepient 'in the mail' and the recipient clicks on their mailbox icon in order to view messages that have been sent to them. The recipient can decide which messages to accept or reject and can categorize them and post them across different personal 'walls' which are labelled by the user's preference. The message they recieve comes on a sticky note and that sticky note is then tacked on the wall they select giving each user a wall of notes they can come back to anytime for encouragement! Each sticky note can be clicked on to view the entire message. If a recipient user is not on the app might have a way to connect to the user's texting app and creating the sticky note template note so that they are still able to send the note along with a link to the app!

## Milestones:

Week 4
Completed Milestones: (Many Milestones were assigned to Week 5 but I was able to complete during Week 4)
•	Set up all Fragment and Activity layouts (Compose, Inbox, Main Wall, Profile fragments) and Sign Up, Login, Main Activities, MessageDetails Activity and Buttons.
•	Set up Parse User login, logout, and sign-up with persistence.
•	Set up User and Message classes and implemented necessary fields
•	Added all the necessary RecyclerViews (for Inbox and MainWall).
•	Implemented Compose and Send message feature.
•	Implemented User's inbox and populated with Messages received.
•	Implemented an AutoCompleteTextView when selecting who to send a message to so that the user can see all the users they can message.
•	Implemented basic Profile screen and using camera to set profile picture
Completed Stretch Goals: 
•	Added Sentiment Analysis API and implemented being able to retrieve a composed message’s positive, negative, and neutral percentage when the user clicks the send button.
•	Updated inbox recyclerView with card layout. A message view can be swiped right to delete and Snackbar message gives the option to undo delete.
•	Updated profile fragment layout to display field for number of messages sent.
•	Added Space tab Layout to UI.
•	Added using Toolbar as an action bar in order to display search icon and implement searching in Main Wall RecyclerView.
•	Finished implementing search bar and filtering in Main wall.
•	Used a Motivational API to get and populate a motivational quote on the profile screen.

Week 5 (Many Milestones were assigned to Week 6 but I was able to complete during Week 5)
Completed Milestones: 
•	Implemented displaying to the user a dialog box if the Sentiment Analysis API returns that a message about to be sent is negative. Dialog box asks if they would still want to send a message if negative percentage is greater than 50%
•	Set up data communication between DialogBox fragments and the parent fragment using a listener interface.
•	Disabled swiping in viewpager (by creating a custom viewpager) in order to use swiping to delete a message from inbox.
•	Updated profile picture click to prompt the user if they want to take a photo or choose a photo from their gallery. Added SelectCameraFragment dialogFragment to do this. Also needed to add a verifyStoragePermissions method to ask the user for permission to access their gallery if it isn't already allowed.
•	Finished creating dialog fragment called from Inbox Fragment to let a user decide which wall they want to pin a note to (memories, kudos, or goals) which updates back4app accordingly (added three boolean columns for each wall).
•	Added dropdown menu to MainWallFragment which allows users to decide which wall of sticky notes they want to see with onOptionsItemSelected filtering the messages based on the option/wall selected by creating helper filter methods for each wall.
Completed Stretch Goals: 
•	Added second sentiment analysis API for more accurate negative sentiment detection between the two APIs.
•	Added Perspective API which shows toxicity value of a message.
•	Updated a message as read if a user clicks on a message in their inbox. Visually grays out that message when the screen is refreshed.
•	Added animations to Main Wall Screen and Message Details Screen.
•	Added water droplet swipe down and refresh
•	Made Sticky notes on each wall different colors. Main wall background added. Added Interface to the MainWallFragmentAdapter in order to pass information from the MainWallFragment to the bind function in the adapter in order to change the sticky note colors based on the wall being displayed.
•	Updated MainWallFragment so that when a wall is selected and then search is used then we search the sticky notes within that specific wall.
•	Implemented autofilling a random compliment if a user decides to press the random compliment button.

Week 6 (Many Milestones were assigned to Week 7 but I was able to complete during Week 6)
Completed Milestones: 
•	Finished implementing Facebook SDK login where a user is automatically signed up with their name as their username if a first-time user and otherwise logged in automatically (after entering their fb login info) if they already have an account.
•	Added a onOptionsItemSelected case in the Main Wall for when the search icon is pressed so that it doesn't automatically refresh to showing all the messages (adapter filter set to only use messages of the current wall).
•	Added scrolling to the details sticky note so that if the user receives a really long message that doesn't fit on a sticky note they can still scroll through the text on the sticky note.
•	Have finished all the required CodePath listed elements.
Completed Stretch Goals: 
•	Added a send message icon on the ComposeMessage screen where if a user want to send a message to a user that's not on the app they have the option to still compose the message and then click the send text message icon which will allow the user to share it via email/text/other apps. Added a layout for setting up a sticky note graphic where the message the user composes is the text within the sticky note along with a message on top where it says that 'you were invited by [user] to join the app!
•	Updated inbox screen so that it shows a graphic first that says Loading messages... followed by how many unread messages there are.
•	Added ‘add friend’ and ‘add group’ buttons on profile screen and set up FriendRequest class.
•	Finished complete functionality of sending friend requests, accepting friend requests, rejecting friend requests, and unfriending. (Pending/Friended/Unfriended/Rejected).
•	Finished Undo option on Inbox screen where a user can click Undo multiple times to undo delete previously deleted messages (compared to the Snackbar undo delete option which only lets you undo delete the most recently deleted message). Once a user refreshes the screen, the messages swiped that weren’t undo-ed are deleted permanently.
•	Finished adding multi-selection Dialog instead for deciding which walls to pin a sticky note to instead of the single option Dialog.

