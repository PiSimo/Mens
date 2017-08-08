# Mens
Android brain game that tries to spot Memory Leaks with Neural Network.

<img src="https://user-images.githubusercontent.com/17238972/29034008-224e4d32-7b97-11e7-89b2-e9e57f411e35.png" width=300 height=500 /> <img src="https://user-images.githubusercontent.com/17238972/29034010-2250316a-7b97-11e7-9a03-2c6fb02f5b11.png" width=300 height=500 />
<br />
<img src="https://user-images.githubusercontent.com/17238972/29034009-224ecdf2-7b97-11e7-8870-10d82d5487ab.png" width=300 height=500/> <img src="https://user-images.githubusercontent.com/17238972/29034007-22265dfe-7b97-11e7-9a7d-b4cb7899833e.png" width=300 height=500/>
<br />

# How does it work ?
The game is made of different questions each of them returns a score. All the scores are passed to a neural network (along with age, gender, test time) that computes the final score.

# Why a Neural Network ?
Well first of all I must admit my addiction to Neural Network, second a 20yo that scores 8/11 isn't the same as a 81yo scoring the same result. So we've collected a bunch of train samples on people with known memory issues and people without any, and  then we trained the neural network in order to compute a weighted final score.

# Status
The project is not on the play store yet. <br />Because of some compatibility issues with android 5.0/5.11, and because we are trying to increase the nn accuracy.<br />
But it works fine on Marshmallow and Nougat, if you want to try the app you can install this <a href="https://github.com/PiSimo/Mens/raw/master/apk/app-flavorRelease-release.apk">apk</a>.<br/>

# Help us
If you notice errors, or you have an idea to make this project better open a new issue.<br />


