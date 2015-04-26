# SendEmailAndroid
Send email from your Anroid using our SendEmailRobust API service

This Android app is a client app for sending emails. It uses our SendEmailRobust API which provides a quite reliable means of sending emails, itself backed by two email providers.

Features:
* A simple interface: FROM, TO, SUBJECT, BODY form and a SEND button
* Add TO addresses from your Contacts library 
* FROM field is pre-populated with your email address at launch

Technical features:
* Supports landscape mode only to faciliate typing and simplify implementation 
* All user input is validated before email is sent
* Empty subject and body is allowed
* Email is sent in a separate thread so GUI thread is not blocked
* The HTTP POST request code is from a snippet found online: https://stackoverflow.com/questions/6218143/how-to-send-post-request-in-json-using-httpclient
* Getting email address from contacts is from a snippet found online: https://stackoverflow.com/questions/11669069/get-email-address-from-contact-list
* Public domain icon from http://www.iconarchive.com/show/blue-bits-icons-by-icojam/mail-arrow-up-icon.html

_Future_
* Add support for CC and BCC. API already supports it. They would be similiar in function to the TO field
* Add support for voice dicatation so user can dictate email via voice
