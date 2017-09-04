/*
This program implements the Point in Polygon test algorithm. There is a modification that has 
been applied.

A polygon is drawn by the user and it is defined by specifying the points like in CvDefPoly.

Algorithm: Point in Polygon
Modification: When mouse is dragged the screen is redrawn to show the latest development.

Uses: Point2D, Tools2D, Triangles

By Devanshu Sheth


*/

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class PointInPolygon extends Frame
{
	public static void main(String[] args) 
	{
                //call constructor in main method
		new PointInPolygon();
	}

	public PointInPolygon() 
	{
            //display Point In Polygon TEST in title bar 
		super("Point In Polygon TEST");
                
                //check if window is resized 
		addWindowListener(new WindowAdapter() {
                    
                        //override the window closing method
                        @Override
			public void windowClosing(WindowEvent e)
			{
                                
				System.exit(0);
			}
		});
                //set size of the canvas
		setSize(1800, 1000);
                //call constructor to Canvas class
		add("Center", new CvPointInPolygon());
		//show method() is deprecated
                //using setVisible
                setVisible(true);
	}
}

//class Point2D to define 2D points
class Point2D
{

float x,y;

//constructor for Point2D
Point2D(float x, float y)
{
this.x = x;
this.y =y;
}


} 

//class Triangle
//used by Tools2D
class Triangle
{  Point2D a, b, c;
   Triangle(Point2D a, Point2D b, Point2D c)
   {  this.a = a; this.b = b; this.c = c;
   }
}

//class Tools2D as given in textbook
class Tools2D
{  static float area2(Point2D a, Point2D b, Point2D c)
   {  
       return (a.x - c.x) * (b.y - c.y) - (a.y - c.y) * (b.x - c.x);
}
   

   static boolean insideTriangle(Point2D a, Point2D b, Point2D c, 
      Point2D p) // ABC is assumed to be counter-clockwise
   {  return
        Tools2D.area2(a, b, p) >= 0 && 
        Tools2D.area2(b, c, p) >= 0 && 
        Tools2D.area2(c, a, p) >= 0;
   }

   static void triangulate(Point2D[] p, Triangle[] tr)
   {  // p contains all n polygon vertices in CCW order.
      // The resulting triangles will be stored in array tr.
      // This array tr must have length n - 2.
      int n = p.length, j = n - 1, iA=0, iB, iC;
      int[] next = new int[n];
      for (int i=0; i<n; i++) 
      {  next[j] = i;
         j = i;
      }
      for (int k=0; k<n-2; k++)
      {  // Find a suitable triangle, consisting of two edges
         // and an internal diagonal:
         Point2D a, b, c;
         boolean triaFound = false;
         int count = 0;
         while (!triaFound && ++count < n)
         {  iB = next[iA]; iC = next[iB];
            a = p[iA]; b = p[iB]; c = p[iC];
            if (Tools2D.area2(a, b, c) >= 0)
            {  // Edges AB and BC; diagonal AC.
               // Test to see if no other polygon vertex
               // lies within triangle ABC:
               j = next[iC];
               while (j != iA && !insideTriangle(a, b, c, p[j]))
                  j = next[j];
               if (j == iA) 
               {  // Triangle ABC contains no other vertex:
                  tr[k] = new Triangle(a, b, c);
                  next[iA] = iC; 
                  triaFound = true;
               }  
            }  
            iA = next[iA];
         }
         if (count == n)
         {  System.out.println("Not a simple polygon" +
              " or vertex sequence not counter-clockwise.");
            System.exit(1);
         }
      }
   }


   static float distance2(Point2D p, Point2D q)
   {  float dx = p.x - q.x, 
            dy = p.y - q.y;
      return dx * dx + dy * dy;
   }
}


//class CvPointInPolygon which extends the canvas
class CvPointInPolygon extends Canvas
{
    //device coordinates for center
    int centerX, centerY;
	
        //float values for the logical width and height, and the actual values 
        float pixelSize, rWidth = 20.0F, rHeight = 20.0F;

        //used to store count of vertical and horizontal lines
        int vlines, hlines;
        
        //device coordinates for max dimensions and the count of intersections
        int maxX, maxY, count1=0;
        
        //used to store old coordinates for last click
        float x0,y0,x0a,y0a;
        
        //vector is used to store the points of polygon
	Vector v = new Vector();
        
        //boolean values used to check certain conditions
        //ready checks if polygon is finished by pressing back in rectangle
        //check is used to store whether the point is inside or outside
        //start is used to redraw in case of mouse dragged event
        boolean ready = true;
        boolean check = false;
        boolean start = false;
   
        //the Point2D array which will store the points of polygon
        Point2D[] pointlist;
        
        //boolean values for the two conditions
        boolean condition1 =false, condition2 = false;
        
        //the arraylist stores the intersection points
        ArrayList<Point2D> intersectionpointlist = new  ArrayList<>();
        
       
        //constructor for the canvas
        CvPointInPolygon() 
	{
           //add a Mouse Motion listener for the dragging event 
         addMouseMotionListener(new MouseAdapter()
        {
          //check if mouse dragged
          @Override
          public void mouseDragged(MouseEvent evt)
          {
             
              //if start is true, it means the mouse has been dragged
              if(start){
           
             //get new coordinates of the drag
            int x1 = (evt.getX());
            int y1 = (evt.getY());
            
            //this is used to round the point to the nearest "grid point"
            int x2 = (int)(Math.rint((double) x1 / 10) * 10);
            int y2 = (int)(Math.rint((double) y1 / 10) * 10);
            
            //store the logical coordinates of this grid point
            float xA = fx(x2), yA = fy(y2);
            
            
            //check is set false, because we may need to check for point movement and it may no longer
            //have any intersections
            check = false;
            
            //if the polygon has finished
            if(ready){
                
                //new count for intersections points
            count1 = 0;
            
            //give new coordinates to old coordinates
              x0a = xA; y0a = yA;
              x0 = xA; y0 = yA;
             
               int m = v.size();
               
               //check again if polygon has been drawn
               if(m == 0)
                    return;
               else{
                   
                   //if polygon has been drawn , declare the pointlist array to store each point
                 pointlist = new Point2D[m];
                
                 
                 //transfer from vector to the array because it is used as argument for the get intersection method
               for(int i = 0; i < m; i++)
               {
                pointlist[i] = (Point2D)v.elementAt(i);
               }
               
               //call the insidePolygon method to check if any intersections and count them into count1 and return
               //true or false depending on whether point is inside or outside
               check = insidePolygon(new Point2D((xA),(yA)), pointlist);
               
               }}
            
            //revalidate and repaint
            revalidate();
                   repaint();
              }
               
          }
          });
           
         //add a Mouse listener for the dragging event 
        addMouseListener(new MouseAdapter()
      {
         //check if mouse clicked 
         @Override
         public void mouseClicked(MouseEvent evt)
         { 
           //get new coordinates of the click
            int x1 = evt.getX();
            int y1 = evt.getY();
            
            //this is used to round the point to the nearest "grid point"
            int x2 = (int)(Math.rint((double) x1 / 10) * 10);
            int y2 = (int)(Math.rint((double) y1 / 10) * 10);
            
            //store the logical coordinates of this grid point
            float xA = fx(x2), yA = fy(y2);
            
            //check is set false, because we may need to check for point movement and it may no longer
            //have any intersections
            check = false;
           
            // Check to see if Condition1 button was pressed
            if (x1>1560 && x1<1665 && y1>359 && y1<386)
            {	
              //if yes, set condition1 boolean flag to indicate only condition1 must be used
              condition1 = true;
              condition2 = false;
            }
            
            // Check to see if Condition2 button was pressed
            if (x1>1560 && x1<1665 && y1>459 && y1<486)
            {	
                //if yes, set condition2 boolean flag to indicate only condition2 must be used
                condition2 = true;
                condition1 = false;
            }
            
            // Check to see if Both Conditions button was pressed
            //also default
            if (x1>1560 && x1<1665 && y1>559 && y1<586)
            {	
                //if yes, set condition2 boolean flag to false to indicate both conditions must be used
                condition2 = false;
                condition1 = false;
                
            }
            
            //if any of the three cases
            if((!condition1 && condition2) || (!condition2 && condition1) || (!condition1 && !condition2 )){
                
            //if polygon has finished  
            if (ready) 
            {  
               
            //new count for intersections points
            count1 = 0;
            
            //give new coordinates to old coordinates
            x0a = xA; y0a = yA;
            x0 = xA; y0 = yA;
            
            //get size of vector
            int m = v.size();
               
              //if polygon has been drawn , declare the pointlist array to store each point
               pointlist = new Point2D[m];
               for(int i = 0; i < m; i++)
               {
                pointlist[i] = (Point2D)v.elementAt(i);
               }
               
               //call the insidePolygon method to check if any intersections and count them into count1 and return
               //true or false depending on whether point is inside or outside
               check = insidePolygon(new Point2D((xA),(yA)), pointlist);
               ready = false;
            }

            //calculate dx and dy which is used to check how far point is from original point
            float dx = xA - x0, dy = yA - y0;
            if (v.size() > 0 && dx * dx + dy * dy < 8 * pixelSize * pixelSize)
            {
                //if click was inside, set start and ready flags
                ready = true;
                start = true;
            }
            //otherwise add new point
            else 
            {
                    v.addElement(new Point2D(xA, yA));
            }}
            //repaint canvas
            repaint();
         }
      });
	}
       
//method to check whether inside or outside polygon
//taken from text book and modified to suit needs
public boolean insidePolygon(Point2D p, Point2D[] pol )
{
   
    int n = pol.length, j = n-1;
    boolean b = false;
    float x = p.x, y = p.y;
    
  
    Point2D p2 = new Point2D(maxX, y);
    
    
    for(int i = 0; i <n; i++)
    {
        //if both conditions button is pressed or none are, use default to find using Point in Polygon Test
        if(!condition1 && !condition2)
        {
        if(pol[j].y <= y && y < pol[i].y && Tools2D.area2(pol[j], pol[i], p) > 0 || pol[i].y <= y && y < pol[j].y && Tools2D.area2(pol[i], pol[j], p) > 0)
        {
            Point2D p3 = pol[i];
            Point2D p4 = pol[j];
            
            //get intersection of two lines (horizontal line we draw and polygon edge)
            Point2D pint = getLineIntersection(p, p2, p3, p4);
            //add it to the arraylist
            intersectionpointlist.add(pint);
            
            //increase count1
            count1++;
            b = !b;
            
        }
        j = i;}
        
        //
        else if(condition1 && !condition2)
        {
        if(pol[j].y <= y && y < pol[i].y && Tools2D.area2(pol[j], pol[i], p) > 0 )
        {
             //get intersection of two lines (horizontal line we draw and polygon edge)
            Point2D p3 = pol[i];
            Point2D p4 = pol[j];
            //add it to the arraylist
            Point2D pint = getLineIntersection(p, p2, p3, p4);
            intersectionpointlist.add(pint);
            //increase count1
            count1++;
            b = !b;
            
        }
        j = i;}
        else if(!condition1 && condition2)
        {
        if(pol[i].y <= y && y < pol[j].y && Tools2D.area2(pol[i], pol[j], p) > 0)
        {
            //get intersection of two lines (horizontal line we draw and polygon edge)
            Point2D p3 = pol[i];
            Point2D p4 = pol[j];
            //add it to the arraylist
            Point2D pint = getLineIntersection(p, p2, p3, p4);
            intersectionpointlist.add(pint);
            //increase count1
            count1++;
            b = !b;
            
        }
        j = i;}
        
        
    }
    return b;
    
    }
    
    //method to get intersection of two lines
   
  public Point2D getLineIntersection(Point2D p1, Point2D p2, Point2D p3, Point2D p4) {
      
      //get the x and y coordinates for the 4 points
      float x1 = p1.x;
      float y1 = p1.y;
      
      float x2 = p2.x;
      float y2 = p2.y;
      
      float x3 = p3.x;
      float y3 = p3.y;
      
      float x4 = p4.x;
      float y4 = p4.y;
      
      //get determinants for 2*2
      double det12 =  determinant(x1, y1, x2, y2);
      double det34 =  determinant(x3, y3, x4, y4);
      
      //get differences of the values
      double x1x2diff = x1 - x2;
      double y1y2diff = y1 - y2;
      double x3x4diff = x3 - x4;
      double y3y4diff = y3 - y4;
      
      //get determinant of the differences
      double det1 =  determinant(x1x2diff, y1y2diff, x3x4diff, y3y4diff);
      if (det1 == 0){
         // the denominator is zero, parallel lines.
         return null;
      }
      //solve for x and y
      double x = (determinant(det12, x1x2diff,det34, x3x4diff) / det1);
      double y =  (determinant(det12, y1y2diff,det34, y3y4diff) /det1);
      return new Point2D((float)x, (float)y);
   }
 
  //get determinant of 2*2 matrix
   protected static double determinant(double a, double b, double c, double d) {
      return a * d - b * c;
   }


//get dimensions of the screen
	protected void initgr()
            {
              Dimension d = getSize();
               maxX = d.width - 1;
               maxY = d.height - 1;
              pixelSize = Math.max(rWidth/maxX, rHeight/maxY);
              centerX = maxX/2; centerY = maxY/2;
              
              
              //calculate number of lines to draw 
              //both horizontal and vertical
              hlines = maxY/10;
              vlines = maxX/10;
              
            }

	//Conversion from logical coordinates to device coordinates
        //and from device coordinates to logical coordinates
	int iX(float x)
	{
		return Math.round(centerX + x / pixelSize);
	}

	int iY(float y)
	{
		return Math.round(centerY - y / pixelSize);
	}
	
	float fx(int X)
	{
		return (X - centerX) * pixelSize;
	}

	float fy(int Y)
	{
		return (centerY - Y) * pixelSize;
	}


        //override the paint method
        @Override
	public void paint(Graphics g)
	{   
        
                //call the method to declare the pixelSize 
		initgr();
                
                //define size of the grid (10 pixels wide as required)
                int dGrid = 10, k;
                
                
                // Draws Condition 1 button
		g.setColor(Color.red);
		g.drawRect(1560, 360, 105, 25);
		g.setColor(Color.black);
		g.drawRect(1559, 359, 107, 27);
		g.drawRect(1561, 361, 103, 23);
		g.drawString("Condition 1", 1575, 377);

                 // Draws Condition 2 button
		g.setColor(Color.red);
		g.drawRect(1560, 460, 105, 25);
		g.setColor(Color.black);
		g.drawRect(1559, 459, 107, 27);
		g.drawRect(1561, 461, 103, 23);
		g.drawString("Condition 2", 1575, 477);
                
                 // Draws Both Conditions button
		g.setColor(Color.red);
		g.drawRect(1560, 560, 105, 25);
		g.setColor(Color.black);
		g.drawRect(1559, 559, 107, 27);
		g.drawRect(1561, 561, 103, 23);
		g.drawString("Both Conditions", 1575, 577);
                
                //define the drawing rectangle on canvas
                 int left = iX(-rWidth/2), right = iX(rWidth/2),
      bottom = iY(-rHeight/2), top = iY(rHeight/2);
      g.drawRect(left, top+1, right - left, bottom - top - 1);
                
               //very light gray for most visibility when drawing on top of it with black
                g.setColor(new Color(211,211,211));
                
                //draw the horizontal lines, only between the drawing rectangle extremes
              for ( k = 0; k < hlines; k++)
              {
               
                  g.drawLine(left, k * dGrid , right, k * dGrid );
              }

              //draw the vertical lines, only between the drawing rectangle extremes
        for ( k = 0; k < vlines; k++)
        {
            if(k*dGrid>= left && k*dGrid<=right)
            g.drawLine(k*dGrid , 0, k* dGrid , maxY);
        }
        
        //switch back to black
          g.setColor(Color.black);
     
      
          //display condition 1 is selected for user
      if(condition1)
      {
          g.drawString("Only Condition 1 selected", 1540, 150);
          
      }
      //display condition 2 is selected for user
      if(condition2)
      {
          g.drawString("Only Condition 2 selected", 1540, 150);
      }
      
      //display Both conditions are selected for user
      if(!condition2 && !condition1)
      {
          g.drawString("Both conditions selected", 1540, 150);
      }
      
      //get size of vector
      int n = v.size();
      
      //if zero , do nothing
      if (n == 0){
          return;
      }
    
      //if ready and check are true, there are intersection points
      //draw them in blue
      if(ready)
      {
          if(check)
          {
              for(Point2D p : intersectionpointlist)
              {
                 
                  //draw a circle around and offset its value by few pixels
                  //this is done to center the rectangle
                  g.setColor(Color.blue);
                  g.fillOval(iX(p.x)-3, iY(p.y)-3, 7, 7);
                  g.setColor(Color.black);
                
              }
              
              //if both conditions was selected, it should display inside and outside the polygon
              //as well as count the intersections
              if(!condition1 && !condition2){
                   g.drawString("P is inside the polygon", 1540, 50);
                   g.drawString("There are  "+count1+" Intersections", 1540, 100);
              }
              //count and display number of intersections
              else{
                  g.drawString("There are  "+count1+" Intersections", 1540, 100);
              }
          }
    }   
    
      //if ready then, draw a horizontal line to the right
      if(ready){
      g.setColor(Color.red);
          g.drawLine(iX(x0a), iY(y0a), iX(maxX), iY(y0a));
          g.setColor(Color.black);}
      
      //clear the intersection points list at start so list does not display old intersection points if moved
        intersectionpointlist.clear();
      
        //get element at vector inex 0
      Point2D a = (Point2D)(v.elementAt(0)); 
      // Show tiny rectangle around first vertex:
      g.drawRect(iX(a.x)-2, iY(a.y)-2, 8, 8);
      for (int i=1; i<=n; i++)
      {  
          if (i == n && !ready) {
              break;
          }
          //draw polygon edges
         Point2D b = (Point2D)(v.elementAt(i % n));
         g.drawLine(iX(a.x), iY(a.y), iX(b.x), iY(b.y));
         a = b;
      }
    
	}
	

       

}
