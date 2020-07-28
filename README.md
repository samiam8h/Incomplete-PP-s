# Incomplete-PP-s
This is for my uncompleted (and therefore not valuable) Personal Projects.

The only project contained here at the moment is "Neural Circuit Simulator"

Some good material to wet your tastebuds for this project:
  https://www.youtube.com/watch?v=obAHnwp9tOM
  https://www.youtube.com/watch?v=QJ8AW5pi2T4

More advanced material:
  https://www.coursera.org/learn/computational-neuroscience
  Paul Miller, An Introductory Course in Computational Neuroscience

  Research in Computational Neuroscience is done from many different angles, some focus on the "big picture" computation involving different parts of the brain. Others focus on
single neurons themselves, and try to delve into every single detail, no matter how trivial. Of course even a single neuron is an extremely difficult object to model with high 
accuracy, it being a chemical object it is prone to being stochastic, and so it is modeled as such, requiring complex programs. Though these programs are very high resolution, 
it is absolutely horrible at modeling neurons at larger scales because it needs so much processing power, and this very complexity you put in the system clouds the 
core computational truths that actually matters. For example, fat tissues and veins obviously influence the formation and behaviour of neurons, and yet are irrelevant to the 
computation itself,therefore these variables "fat" and "blood" can be discarded from the models. Likewise in a computer, the metals, plastics, and varying temperatures are
irrelevant, only the logic gates and architecure are important for the computation.

  A current problem in research, specifically research in the sub field of Neural Circuitry, is that enormous time and energy is spent essentially "guess-and-checking" what 
various neural circuits in the nervous system do, and how they do it. How a circuit takes information and outputs new information is a back breaking question. New tools are 
required to make this process easier.

  Neural Circuit simulator attempts to simplify/discretize, and model the interactions of neurons in a neural circuit such that the essential elements of computation 
are preserved.  On top of this, a genetic algorithm can be used to sift through the near infinite variety of neural circuits to find one (or several) "breeds"
that takes your set of inputs and gives the correct output for every instance.

ToDo:
-IncidenceList details
     -All neurons must be connected to each other. ie no stray neurons.
-Step() needs fixing, neuron should be off if it is not activated by other neurons by the next Step.
-Make "example circuits" tab to show off real life neural circuits the program has sucessfully made.
-!Make readOutput(), if an output neuron is activated record the "total output"(whatever this means).
-Color axons based on weather they are exitatory or inhibitory.
-GUI has graphs and tools to help you visualize what's going on.

Future version details to work out:
-Determine weather reverberating NCs are legal in the models, or weather it should be an added option.
-Currently the time intervals are dicrete and not continuous, meaning only digital(and not analog) computation can be modeled.

  *A simple test case for the Genetic Algorithm would be to see if it can produce the neual circuit equivalent of a "half adder", 
the essential component to add 2 bits in a computer.

