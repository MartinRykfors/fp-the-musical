### Functional FM-Synthesis with Overtone

This repo contains the slides and code from my talk about FM-Synthesis using Overtone, given at the Brighton Functional Programming Meetup, Jan 20 2015.

The presentation was given using the emacs package [epresent](https://github.com/eschulte/epresent).

### The code

I've provided a modified version of the code written during the talk.
To run it, start cider, then press C-c C-l to load the entire file. The sequencer will start playing and you will be able to change the sound of the synthesizer by altering the values passed to it in the function called trigger.
By changing the contents of the vector of vectors called choices, you can change how the sequence is generated.

### The faders

The code for evaluating the faders are included in a separate file. I have taken them out of the overtone file, since you need special keybindings to use them. If you are interested in using them, you can download ryk-mode from my [emacs config](https://github.com/MartinRykfors/.emacs.d).
