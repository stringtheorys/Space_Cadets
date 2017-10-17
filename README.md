# Space_Cadets
Uni of Southampton Prog Club

Challenge 1


<b>Challenge 2</b>

The challenge was to create esoteric programming language, I developped an interpreter for a language called Paintf*ck.
Paintf*ck is related to Smallf*ck and Brainf*ck in the style however has a 2d output grid
The language is Turing-complete over an infinite grid. If given a finite grid as in the prototype interpreter, it acts like a finite state machine.

The Commands

    n - Move data pointer north
    s - Move data pointer south
    w - Move data pointer west
    e - Move data pointer east
    * - Flip data pointer cell Black -> White or White -> Black
    [ - While data is not 0
    ] - End of While loop
    
Example Progams

Change the screen to black
*[s[e]*]

![Alt Text](https://media.giphy.com/media/vFKqnCdLPNOKc/giphy.gif)
