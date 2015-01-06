ledviz2
=======

The Idea is to have a more or less generic piece of software to visualize audio data. The functionality is kept to this core aspect. 

Therefore ledviz2 consists of several generic interfaces which can be enhanced to support a wide range of media players and not be specialised to exactly one. There are some sample implementations for MPD (Music Plaing Deamon) and iTunes (only Windows) aswell as a more generic Dummy interface which can be used for every audio source.

A generic interface is also provided for the analysis of the audio stream. A sample implementation using BASS Audio Library is given. The same goes for the way the data is visualized. The only thing that cannot be cahnaged is the display style. This is a LED Matrix all the time.

To use this software you need to accomplish several steps:

1. Install a VAC (Virtual Audio Cable) to redirect the audio Output from your media player to an audio input. To give the software the possibility to read the data. Alternatively you can implement your own version of the AttachedSoundProvider Interface for your MediaPlayer.
2. If youre using MPD of iTunes you can use the sample implementations; you just need to adjust the config file accordingly. Note that the iTunes implementation makes use of the Windows COM interface (AKA ActiveX) to retrive some Meta data like the currently playing track name etc. If youre not using one of the players listed above, you can either use the Dummy implementation or implement your own for the player you want. If you do so, please provide your implementation so it can be added to the sources.
3. Adjust the configuration to your needs
4. Go!
5. Have fun ;)
