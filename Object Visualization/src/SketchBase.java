//****************************************************************************
// SketchBase.  
//****************************************************************************
// Comments : 
//   Subroutines to manage and draw points, lines an triangles
//
// History :
//	 Dec 2015 Modified by Jia Yao to incorporate using depth buffer to render 3D points
//   Aug 2014 Created by Jianming Zhang (jimmie33@gmail.com) based on code by
//   Stan Sclaroff (from CS480 '06 poly.c)

import java.awt.image.BufferedImage;
import java.util.*;

public class SketchBase 
{
	public SketchBase()
	{
		// deliberately left blank
	}
	
	/**********************************************************************
	 * Draws a point.
	 * This is achieved by changing the color of the buffer at the location
	 * corresponding to the point. 
	 * 
	 * @param buff
	 *          Buffer object.
	 * @param p
	 *          Point to be drawn.
	 */
	public static void drawPoint(BufferedImage buff, Point3D p)
	{
		// clip what's outside the screen
		if(p.x>=0 && p.x<buff.getWidth() && p.y>=0 && p.y < buff.getHeight())
			buff.setRGB(p.x, buff.getHeight()-p.y-1, p.c.getRGB_int());	
	}
	
	/**********************************************************************
	 * Draws a line segment using DDA
	 * interpolating RGB color along line segment
	 * 
	 * @param buff
	 *          Buffer object.
	 * @param p1
	 *          First given endpoint of the line.
	 * @param p2
	 *          Second given endpoint of the line.
	 */
	public static void drawLine(BufferedImage buff, DepthBuffer depth, Point3D p1, Point3D p2)
	{
	    int x0=p1.x, y0=p1.y, z0 = p1.z;
	    int xEnd=p2.x, yEnd=p2.y, zEnd = p2.z;
	    int dx = Math.abs(xEnd - x0),  dy = Math.abs(yEnd - y0);

	    if(dx==0 && dy==0)
	    {
	    	if (p1.x>=0 && p1.x<buff.getWidth() && p1.y>=0 && p1.y < buff.getHeight() && p1.z >= depth.mat[p1.x][buff.getHeight()-p1.y-1]){
	    		buff.setRGB(p1.x, buff.getHeight()-p1.y-1, p1.c.getRGB_int());
	    		depth.mat[p1.x][buff.getHeight()-p1.y-1] = p1.z;
	    		
	    	}
	    	return;
	    }
	    
	    // if slope is greater than 1, then swap the role of x and y
	    boolean x_y_role_swapped = (dy > dx); 
	    if(x_y_role_swapped)
	    {
	    	x0=p1.y; 
	    	y0=p1.x;
	    	xEnd=p2.y; 
	    	yEnd=p2.x;
	    	dx = Math.abs(xEnd - x0);
	    	dy = Math.abs(yEnd - y0);
	    }
	    
	    // initialize the decision parameter and increments
	    int p = 2 * dy - dx;
	    int twoDy = 2 * dy,  twoDyMinusDx = 2 * (dy - dx);
	    int x=x0, y=y0;
	    
	    // set step increment to be positive or negative
	    int step_x = x0<xEnd ? 1 : -1;
	    int step_y = y0<yEnd ? 1 : -1;
	    
	    // deal with setup for color interpolation
	    // first get r,g,b integer values at the end points
	    int r0=p1.c.getR_int(), rEnd=p2.c.getR_int();
	    int g0=p1.c.getG_int(), gEnd=p2.c.getG_int();
	    int b0=p1.c.getB_int(), bEnd=p2.c.getB_int();
	    
	    // compute the change in r,g,b 
	    int dr=Math.abs(rEnd-r0), dg=Math.abs(gEnd-g0), db=Math.abs(bEnd-b0);
	    
	    // set step increment to be positive or negative 
	    int step_r = r0<rEnd ? 1 : -1;
	    int step_g = g0<gEnd ? 1 : -1;
	    int step_b = b0<bEnd ? 1 : -1;
	    
	    // compute whole step in each color that is taken each time through loop
	    int whole_step_r = step_r*(dr/dx);
	    int whole_step_g = step_g*(dg/dx);
	    int whole_step_b = step_b*(db/dx);
	    
	    // compute remainder, which will be corrected depending on decision parameter
	    dr=dr%dx;
	    dg=dg%dx; 
	    db=db%dx;
	    
	    // initialize decision parameters for red, green, and blue
	    int p_r = 2 * dr - dx;
	    int twoDr = 2 * dr,  twoDrMinusDx = 2 * (dr - dx);
	    int r=r0;
	    
	    int p_g = 2 * dg - dx;
	    int twoDg = 2 * dg,  twoDgMinusDx = 2 * (dg - dx);
	    int g=g0;
	    
	    int p_b = 2 * db - dx;
	    int twoDb = 2 * db,  twoDbMinusDx = 2 * (db - dx);
	    int b=b0;
	    
	    // initialize step increment for z
	    int z = p1.z;
	    float actualz = (float)z;
	    float dz = (zEnd - z0) / (float)dx; 
	    
	    // draw start pixel
	    if(x_y_role_swapped)
	    {
	    	if(x>=0 && x<buff.getHeight() && y>=0 && y<buff.getWidth() && (z >= depth.mat[y][buff.getHeight()-x-1])){
	    		buff.setRGB(y, buff.getHeight()-x-1, (r<<16) | (g<<8) | b);
	    		//System.out.println(z + " " + depth.mat[y][buff.getHeight()-x-1] );
	    		depth.mat[y][buff.getHeight()-x-1] = z;
	    	}
	    }
	    else
	    {
	    	if(y>=0 && y<buff.getHeight() && x>=0 && x<buff.getWidth() && (z >= depth.mat[x][buff.getHeight()-y-1])){
	    		buff.setRGB(x, buff.getHeight()-y-1, (r<<16) | (g<<8) | b);
	    		//System.out.println(z + " " + depth.mat[x][buff.getHeight()-y-1] );
	    		depth.mat[x][buff.getHeight()-y-1] = z;
	    	}		
	    }
	    
	    while (x != xEnd) 
	    {
	    	// increment x and y
	    	x+=step_x;
	    	if (p < 0)
	    		p += twoDy;
	    	else 
	    	{
	    		y+=step_y;
	    		p += twoDyMinusDx;
	    	}
		        
	    	// increment r by whole amount slope_r, and correct for accumulated error if needed
	    	r+=whole_step_r;
	    	if (p_r < 0)
	    		p_r += twoDr;
	    	else 
	    	{
	    		r+=step_r;
	    		p_r += twoDrMinusDx;
	    	}
		    
	    	// increment g by whole amount slope_b, and correct for accumulated error if needed  
	    	g+=whole_step_g;
	    	if (p_g < 0)
	    		p_g += twoDg;
	    	else 
	    	{
	    		g+=step_g;
	    		p_g += twoDgMinusDx;
	    	}
		    
	    	// increment b by whole amount slope_b, and correct for accumulated error if needed
	    	b+=whole_step_b;
	    	if (p_b < 0)
	    		p_b += twoDb;
	    	else 
	    	{
	    		b+=step_b;
	    		p_b += twoDbMinusDx;
	    	}
		    
	    	// increment z
	    	actualz += dz;
	    	z = Math.round(actualz);
	    	
	    	if(x_y_role_swapped)
		    {
		    	if(x>=0 && x<buff.getHeight() && y>=0 && y<buff.getWidth() && (z >= depth.mat[y][buff.getHeight()-x-1])){
		    		buff.setRGB(y, buff.getHeight()-x-1, (r<<16) | (g<<8) | b);
		    		//System.out.println(z + " " + depth.mat[y][buff.getHeight()-x-1] );
		    		depth.mat[y][buff.getHeight()-x-1] = z;
		    	}
		    }
		    else
		    {
		    	if(y>=0 && y<buff.getHeight() && x>=0 && x<buff.getWidth() && (z >= depth.mat[x][buff.getHeight()-y-1])){
		    		buff.setRGB(x, buff.getHeight()-y-1, (r<<16) | (g<<8) | b);
		    		//System.out.println(z + " " + depth.mat[x][buff.getHeight()-y-1] );
		    		depth.mat[x][buff.getHeight()-y-1] = z;
		    	}		
		    }
	    }
	}

	/**********************************************************************
	 * Draws a filled triangle. 
	 * The triangle may be filled using flat fill or smooth fill. 
	 * This routine fills columns of pixels within the left-hand part, 
	 * and then the right-hand part of the triangle.
	 *   
	 *	                         *
	 *	                        /|\
	 *	                       / | \
	 *	                      /  |  \
	 *	                     *---|---*
	 *	            left-hand       right-hand
	 *	              part             part
	 *
	 * @param buff
	 *          Buffer object.
	 * @param p1
	 *          First given vertex of the triangle.
	 * @param p2
	 *          Second given vertex of the triangle.
	 * @param p3
	 *          Third given vertex of the triangle.
	 * @param do_smooth
	 *          Flag indicating whether flat fill or smooth fill should be used.                   
	 */
	public static void drawTriangle(BufferedImage buff, DepthBuffer depth, Point3D p1, Point3D p2, Point3D p3, boolean do_smooth)
	{
	    // sort the triangle vertices by ascending x value
	    Point3D p[] = sortTriangleVerts(p1,p2,p3);
	    
	    int x; 
	    float y_a, y_b, z_a, z_b;
	    float dy_a, dy_b, dz_a, dz_b;
	    float dr_a=0, dg_a=0, db_a=0, dr_b=0, dg_b=0, db_b=0;
	    
	    Point3D side_a = new Point3D(p[0]), side_b = new Point3D(p[0]);
	    
	    if(!do_smooth)
	    {
	    	side_a.c = new ColorType(p1.c);
	    	side_b.c = new ColorType(p1.c);
	    }
	    
	    y_b = p[0].y;
	    dy_b = ((float)(p[2].y - p[0].y))/(p[2].x - p[0].x);
	    z_b = p[0].z;
	    dz_b = ((float)(p[2].z - p[0].z))/(p[2].x - p[0].x);
	    
	    if(do_smooth)
	    {
	    	// calculate slopes in r, g, b for segment b
	    	dr_b = ((float)(p[2].c.r - p[0].c.r))/(p[2].x - p[0].x);
	    	dg_b = ((float)(p[2].c.g - p[0].c.g))/(p[2].x - p[0].x);
	    	db_b = ((float)(p[2].c.b - p[0].c.b))/(p[2].x - p[0].x);
	    }
	    
	    // if there is a left-hand part to the triangle then fill it
	    if(p[0].x != p[1].x)
	    {
	    	y_a = p[0].y;
	    	dy_a = ((float)(p[1].y - p[0].y))/(p[1].x - p[0].x);
	    	z_a = p[0].z;
	    	dz_a = ((float)(p[1].z - p[0].z))/(p[1].x - p[0].x);
	    	
	    	if(do_smooth)
	    	{
	    		// calculate slopes in r, g, b for segment a
	    		dr_a = ((float)(p[1].c.r - p[0].c.r))/(p[1].x - p[0].x);
	    		dg_a = ((float)(p[1].c.g - p[0].c.g))/(p[1].x - p[0].x);
	    		db_a = ((float)(p[1].c.b - p[0].c.b))/(p[1].x - p[0].x);
	    	}
		    
		    // loop over the columns for left-hand part of triangle
		    // filling from side a to side b of the span
		    for(x = p[0].x; x < p[1].x; ++x)
		    {
		    	drawLine(buff, depth, side_a, side_b);

		    	++side_a.x;
		    	++side_b.x;
		    	y_a += dy_a;
		    	y_b += dy_b;
		    	z_a += dz_a;
		    	z_b += dz_b;
		    	side_a.y = (int)y_a;
		    	side_b.y = (int)y_b;
		    	side_a.z = (int)z_a;
		    	side_b.z = (int)z_b;
		    	if(do_smooth)
		    	{
		    		side_a.c.r +=dr_a;
		    		side_b.c.r +=dr_b;
		    		side_a.c.g +=dg_a;
		    		side_b.c.g +=dg_b;
		    		side_a.c.b +=db_a;
		    		side_b.c.b +=db_b;
		    	}
		    }
	    }
	    
	    // there is no right-hand part of triangle
	    if(p[1].x == p[2].x)
	    	return;
	    
	    // set up to fill the right-hand part of triangle 
	    // replace segment a
	    side_a = new Point3D(p[1]);
	    if(!do_smooth)
	    	side_a.c =new ColorType(p1.c);
	    
	    y_a = p[1].y;
	    dy_a = ((float)(p[2].y - p[1].y))/(p[2].x - p[1].x);
	    z_a = p[1].z;
	    dz_a = ((float)(p[2].z - p[1].z))/(p[2].x - p[1].x);
	    if(do_smooth)
	    {
	    	// calculate slopes in r, g, b for replacement for segment a
	    	dr_a = ((float)(p[2].c.r - p[1].c.r))/(p[2].x - p[1].x);
	    	dg_a = ((float)(p[2].c.g - p[1].c.g))/(p[2].x - p[1].x);
	    	db_a = ((float)(p[2].c.b - p[1].c.b))/(p[2].x - p[1].x);
	    }

	    // loop over the columns for right-hand part of triangle
	    // filling from side a to side b of the span
	    for(x = p[1].x; x <= p[2].x; ++x)
	    {
	    	drawLine(buff, depth, side_a, side_b);
		    
	    	++side_a.x;
	    	++side_b.x;
	    	y_a += dy_a;
	    	y_b += dy_b;
	    	z_a += dz_a;
	    	z_b += dz_b;
	    	side_a.y = (int)y_a;
	    	side_b.y = (int)y_b;
	    	side_a.z = (int)z_a;
	    	side_b.z = (int)z_b;
	    	if(do_smooth)
	    	{
	    		side_a.c.r +=dr_a;
	    		side_b.c.r +=dr_b;
	    		side_a.c.g +=dg_a;
	    		side_b.c.g +=dg_b;
	    		side_a.c.b +=db_a;
	    		side_b.c.b +=db_b;
	    	}
	    }
	}

	/**********************************************************************
	 * Helper function to bubble sort triangle vertices by ascending x value.
	 * 
	 * @param p1
	 *          First given vertex of the triangle.
	 * @param p2
	 *          Second given vertex of the triangle.
	 * @param p3
	 *          Third given vertex of the triangle.
	 * @return 
	 *          Array of 3 points, sorted by ascending x value.
	 */
	private static Point3D[] sortTriangleVerts(Point3D p1, Point3D p2, Point3D p3)
	{
	    Point3D pts[] = {p1, p2, p3};
	    Point3D tmp;
	    int j=0;
	    boolean swapped = true;
	         
	    while (swapped) 
	    {
	    	swapped = false;
	    	j++;
	    	for (int i = 0; i < 3 - j; i++) 
	    	{                                       
	    		if (pts[i].x > pts[i + 1].x) 
	    		{                          
	    			tmp = pts[i];
	    			pts[i] = pts[i + 1];
	    			pts[i + 1] = tmp;
	    			swapped = true;
	    		}
	    	}                
	    }
	    return(pts);
	}

	//=======================================Phong Shading===========================================
	// modified from drawLine and drawTriangle to do Phong Shading
	public static void drawLinePhong(BufferedImage buff, DepthBuffer depth, LightCollection light, Material mat, Vector3D view_vector, Point3D p1, Point3D p2, Vector3D n1, Vector3D n2){
		int x0=p1.x, y0=p1.y, z0 = p1.z;
	    int xEnd=p2.x, yEnd=p2.y, zEnd = p2.z;
	    int dx = Math.abs(xEnd - x0),  dy = Math.abs(yEnd - y0);
	    float n_x_0 = n1.x, n_y_0 = n1.y, n_z_0 = n1.z;
	    float n_x_end = n2.x, n_y_end = n2.y, n_z_end = n2.z;
	    Vector3D tmp_v = new Vector3D(), tmp_n = new Vector3D();
	    ColorType c;

	    if(dx==0 && dy==0)
	    {
	    	if (p1.x>=0 && p1.x<buff.getWidth() && p1.y>=0 && p1.y < buff.getHeight() && p1.z >= depth.mat[p1.x][buff.getHeight()-p1.y-1]){
	    		tmp_v.set((float)p1.x, (float)p1.y, (float)p1.z);
	    		tmp_n.set(n1.x, n1.y, n1.z);
	    		tmp_n.normalize();
	    		c = light.applyLight(mat, view_vector, tmp_n, tmp_v);
	    		buff.setRGB(p1.x, buff.getHeight()-p1.y-1, c.getRGB_int());
	    		depth.mat[p1.x][buff.getHeight()-p1.y-1] = p1.z;
	    		
	    	}
	    	return;
	    }
	    
	    // if slope is greater than 1, then swap the role of x and y
	    boolean x_y_role_swapped = (dy > dx); 
	    if(x_y_role_swapped)
	    {
	    	x0=p1.y; 
	    	y0=p1.x;
	    	xEnd=p2.y; 
	    	yEnd=p2.x;
	    	dx = Math.abs(xEnd - x0);
	    	dy = Math.abs(yEnd - y0);
	    }
	    
	    // initialize the decision parameter and increments
	    int p = 2 * dy - dx;
	    int twoDy = 2 * dy,  twoDyMinusDx = 2 * (dy - dx);
	    int x=x0, y=y0;
	    
	    // set step increment to be positive or negative
	    int step_x = x0<xEnd ? 1 : -1;
	    int step_y = y0<yEnd ? 1 : -1;
	    
	    // initialize step increment for z
	    int z = z0;
	    float actualz = (float)z;
	    float dz = (zEnd - z0) / (float)dx; 
	    
	    // initialize step increments for x y z of normal
	    float n_x = n_x_0, n_y = n_y_0, n_z = n_z_0;
	    float dn_x = (n_x_end - n_x_0) / (float)dx, dn_y = (n_y_end - n_y_0) / (float)dx, dn_z = (n_z_end - n_z_0) / (float)dx; 
	    
	    // draw start pixel
	    if(x_y_role_swapped)
	    {
	    	if(x>=0 && x<buff.getHeight() && y>=0 && y<buff.getWidth() && (z >= depth.mat[y][buff.getHeight()-x-1])){
	    		tmp_v.set((float)y, (float)x, (float)z);
	    		tmp_n.set(n_x, n_y, n_z);
	    		tmp_n.normalize();
	    		c = light.applyLight(mat, view_vector, tmp_n, tmp_v);
	    		buff.setRGB(y, buff.getHeight()-x-1, c.getRGB_int());
	    		depth.mat[y][buff.getHeight()-x-1] = z;
	    	}
	    }
	    else
	    {
	    	if(y>=0 && y<buff.getHeight() && x>=0 && x<buff.getWidth() && (z >= depth.mat[x][buff.getHeight()-y-1])){
	    		tmp_v.set((float)x, (float)y, (float)z);
	    		tmp_n.set(n_x, n_y, n_z);
	    		tmp_n.normalize();
	    		c = light.applyLight(mat, view_vector, tmp_n, tmp_v);
	    		buff.setRGB(x, buff.getHeight()-y-1, c.getRGB_int());
	    		depth.mat[x][buff.getHeight()-y-1] = z;
	    	}		
	    }
	    
	    while (x != xEnd) 
	    {
	    	// increment x and y
	    	x+=step_x;
	    	if (p < 0)
	    		p += twoDy;
	    	else 
	    	{
	    		y+=step_y;
	    		p += twoDyMinusDx;
	    	}
		        
	    	// increment z
	    	actualz += dz;
	    	z = Math.round(actualz);
	    	
	    	// increment n_x, n_y, n_z
	    	n_x += dn_x;
	    	n_y += dn_y;
	    	n_z += dn_z;
	    	
	    	if(x_y_role_swapped)
		    {
		    	if(x>=0 && x<buff.getHeight() && y>=0 && y<buff.getWidth() && (z >= depth.mat[y][buff.getHeight()-x-1])){
		    		tmp_v.set((float)y, (float)x, (float)z);
		    		tmp_n.set(n_x, n_y, n_z);
		    		tmp_n.normalize();
		    		c = light.applyLight(mat, view_vector, tmp_n, tmp_v);
		    		buff.setRGB(y, buff.getHeight()-x-1, c.getRGB_int());
		    		depth.mat[y][buff.getHeight()-x-1] = z;
		    	}
		    }
		    else
		    {
		    	if(y>=0 && y<buff.getHeight() && x>=0 && x<buff.getWidth() && (z >= depth.mat[x][buff.getHeight()-y-1])){
		    		tmp_v.set((float)x, (float)y, (float)z);
		    		tmp_n.set(n_x, n_y, n_z);
		    		tmp_n.normalize();
		    		c = light.applyLight(mat, view_vector, tmp_n, tmp_v);
		    		buff.setRGB(x, buff.getHeight()-y-1, c.getRGB_int());
		    		depth.mat[x][buff.getHeight()-y-1] = z;
		    	}		
		    }
	    }
	}
	
	public static void drawTrianglePhong(BufferedImage buff, DepthBuffer depth, LightCollection light, Material mat, Vector3D view_vector, Point3D p0, Point3D p1, Point3D p2, Vector3D n0, Vector3D n1, Vector3D n2){
		
		// sort the triangle vertices (and their corresponding normals) by ascending x value
	    Point3D p[] = {p0, p1, p2};
	    Vector3D n[] = {n0, n1, n2};
	    Point3D tmp;
	    Vector3D tmp2;
	    int j=0;
	    boolean swapped = true;
	    
	    while (swapped) 
	    {
	    	swapped = false;
	    	j++;
	    	for (int i = 0; i < 3 - j; i++) 
	    	{                                       
	    		if (p[i].x > p[i + 1].x) 
	    		{                          
	    			tmp = p[i];
	    			p[i] = p[i + 1];
	    			p[i + 1] = tmp;
	    			tmp2 = n[i];
	    			n[i] = n[i + 1];
	    			n[i + 1] = tmp2;
	    			swapped = true;
	    		}
	    	}                
	    }
	    
	    // temporary variables
	    int x; 
	    float y_a, y_b, z_a, z_b, n_x_a, n_y_a, n_z_a, n_x_b, n_y_b, n_z_b;
	    float dy_a, dy_b, dz_a, dz_b;
	    float dn_x_a=0, dn_y_a=0, dn_z_a=0, dn_x_b=0, dn_y_b=0, dn_z_b=0;
	    Point3D side_a = new Point3D(p[0]), side_b = new Point3D(p[0]);
	    Vector3D tmp_n_a = new Vector3D(n[0]), tmp_n_b = new Vector3D(n[0]);
	    
	    // calculate slopes in y, z, and x y z of normal for segment b
	    y_b = p[0].y;
	    dy_b = ((float)(p[2].y - p[0].y))/(p[2].x - p[0].x);
	    z_b = p[0].z;
	    dz_b = ((float)(p[2].z - p[0].z))/(p[2].x - p[0].x);
	    n_x_b = n[0].x;
	    dn_x_b = ((float)(n[2].x - n[0].x))/(p[2].x - p[0].x);
	    n_y_b = n[0].y;
	    dn_y_b = ((float)(n[2].y - n[0].y))/(p[2].x - p[0].x);
	    n_z_b = n[0].z;
	    dn_z_b = ((float)(n[2].z - n[0].z))/(p[2].x - p[0].x);
	    
	    // if there is a left-hand part to the triangle then fill it
	    if(p[0].x != p[1].x)
	    {
	    	// calculate slopes in y, z, and x y z of normal for segment a
	    	y_a = p[0].y;
	    	dy_a = ((float)(p[1].y - p[0].y))/(p[1].x - p[0].x);
	    	z_a = p[0].z;
	    	dz_a = ((float)(p[1].z - p[0].z))/(p[1].x - p[0].x);
	    	n_x_a = n[0].x;
	    	dn_x_a = ((float)(n[1].x - n[0].x))/(p[1].x - p[0].x);
	    	n_y_a = n[0].y;
	    	dn_y_a = ((float)(n[1].y - n[0].y))/(p[1].x - p[0].x);
	    	n_z_a = n[0].z;
	    	dn_z_a = ((float)(n[1].z - n[0].z))/(p[1].x - p[0].x);
	    	
		    // loop over the columns for left-hand part of triangle
		    // filling from side a to side b of the span
		    for(x = p[0].x; x < p[1].x; ++x)
		    {
		    	drawLinePhong(buff, depth, light, mat, view_vector, side_a, side_b, tmp_n_a, tmp_n_b);

		    	++side_a.x;
		    	++side_b.x;
		    	y_a += dy_a;
		    	y_b += dy_b;
		    	z_a += dz_a;
		    	z_b += dz_b;
		    	n_x_a += dn_x_a;
		    	n_x_b += dn_x_b;
		    	n_y_a += dn_y_a;
		    	n_y_b += dn_y_b;
		    	n_z_a += dn_z_a;
		    	n_z_b += dn_z_b;
		    	side_a.y = (int)y_a;
		    	side_b.y = (int)y_b;
		    	side_a.z = (int)z_a;
		    	side_b.z = (int)z_b;
		    	tmp_n_a.x = n_x_a;
		    	tmp_n_b.x = n_x_b;
		    	tmp_n_a.y = n_y_a;
		    	tmp_n_b.y = n_y_b;
		    	tmp_n_a.z = n_z_a;
		    	tmp_n_b.z = n_z_b;
		    }
	    }
	    
	    // there is no right-hand part of triangle
	    if(p[1].x == p[2].x)
	    	return;
	    
	    // set up to fill the right-hand part of triangle 
	    // replace segment a
	    side_a = new Point3D(p[1]);
	    tmp_n_a = new Vector3D(n[1]);
	    
	    // calculate slopes in y, z, and x y z of normal for segment b
	    y_a = p[1].y;
	    dy_a = ((float)(p[2].y - p[1].y))/(p[2].x - p[1].x);
	    z_a = p[1].z;
	    dz_a = ((float)(p[2].z - p[1].z))/(p[2].x - p[1].x);
	    n_x_a = n[1].x;
    	dn_x_a = ((float)(n[2].x - n[1].x))/(p[2].x - p[1].x);
    	n_y_a = n[1].y;
    	dn_y_a = ((float)(n[2].y - n[1].y))/(p[2].x - p[1].x);
    	n_z_a = n[1].z;
    	dn_z_a = ((float)(n[2].z - n[1].z))/(p[2].x - p[1].x);

	    // loop over the columns for right-hand part of triangle
	    // filling from side a to side b of the span
	    for(x = p[1].x; x <= p[2].x; ++x)
	    {
	    	drawLinePhong(buff, depth, light, mat, view_vector, side_a, side_b, tmp_n_a, tmp_n_b);
		    
	    	++side_a.x;
	    	++side_b.x;
	    	y_a += dy_a;
	    	y_b += dy_b;
	    	z_a += dz_a;
	    	z_b += dz_b;
	    	n_x_a += dn_x_a;
	    	n_x_b += dn_x_b;
	    	n_y_a += dn_y_a;
	    	n_y_b += dn_y_b;
	    	n_z_a += dn_z_a;
	    	n_z_b += dn_z_b;
	    	side_a.y = (int)y_a;
	    	side_b.y = (int)y_b;
	    	side_a.z = (int)z_a;
	    	side_b.z = (int)z_b;
	    	tmp_n_a.x = n_x_a;
	    	tmp_n_b.x = n_x_b;
	    	tmp_n_a.y = n_y_a;
	    	tmp_n_b.y = n_y_b;
	    	tmp_n_a.z = n_z_a;
	    	tmp_n_b.z = n_z_b;
	    }
	    
	}
}