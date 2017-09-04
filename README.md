# project-3-cgraphics
Write a Java program to demonstrate the principle of the Point-in-polygon test algorithm.

1) Draw as many horizontal and vertical lines as necessary to divide your canvas into NxM squares, each sized at 10x10 pixels. N and M depend on the size of your canvas. We call every intersection a “grid point”.

2) Adapt the “define a polygon by mouse” program in Chapter 1, such that every mouse click point is placed on the nearest grid point, as a polygon vertex. 
   
3) The click inside the small rectangle will complete the drawing of the polygon, but the next click will NOT start a new polygon. It is, instead, considered a point P to be tested as inside or outside of the polygon. 

4) A red half-line is drawn starting from P towards the right, with all the points interesting with the polygon edges highlighted (flashing or colored blue). The number of intersection points is also displayed on the top-right corner of the canvas.

5) Allow the user to select one of the two sets of inequations (i.e. lower end points or upper end points are counted), as an option on the side of the canvas (interface implementation is at your choice).
   
6) (Bonus 2%) Allow the user to pull (move) P anywhere on the canvas by holding the mouse button, inside or outside of the polygon, while the features in (4) are displayed accordingly during the move. 
   
In addition to producing a working program in Java, you should also explain clearly, as comments on top of your program, how the program is implemented. For a partially working submission, please explain (also as comments) which part works and which part does not.
