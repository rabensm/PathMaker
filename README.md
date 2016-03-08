# PathMaker
Pathmaker is a simple utility for turning 3D paths into g-code for a CNC router. I'm writing this because a CNC project I'm working on needs procedurally generated paths.

Pathmaker does two things:

1. It slices the desired paths into depth layers, with cutting paths in each layer (so the paths will be done in multiple, shallow passes on the CNC router).

2. It turns the cutting paths into g-code.
