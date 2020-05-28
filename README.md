# ProfileView
Profile Image View


https://github.com/pankajmuneshwar/ProfileView/blob/master/Screenshot_1590601305.png

https://github.com/pankajmuneshwar/ProfileView/blob/master/Screenshot_1590601362.png


This is main used for Profile Pic to show with border or without border

Step 1 : Add it in your root build.gradle at the end of repositories:

allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}


Step 2 : Add the dependency

	dependencies {
	        implementation 'com.github.pankajmuneshwar:ProfileView:Tag'
	}
