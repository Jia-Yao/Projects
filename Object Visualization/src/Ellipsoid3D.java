//****************************************************************************
//      Ellipsoid class
//****************************************************************************
// History :
//	 Dec 6, 2015 Modified by Jia Yao to incorporate quaternion rotation
//   Nov 6, 2014 Created by Stan Sclaroff
//

public class Ellipsoid3D
{
	private Vector3D center;
	public float rx, ry, rz;
	private int m,n;
	public Mesh3D mesh;
	private Quaternion Q = new Quaternion();
	
	public Ellipsoid3D(float _x, float _y, float _z, float _rx, float _ry, float _rz, int _m, int _n)
	{
		center = new Vector3D(_x,_y,_z);
		rx = _rx;
		ry = _ry;
		rz = _rz;
		m = _m;
		n = _n;
		initMesh();
	}
	
	public void set_center(float _x, float _y, float _z)
	{
		center.x=_x;
		center.y=_y;
		center.z=_z;
		fillMesh();  // update the triangle mesh
	}
	
	public void set_radius(float _rx, float _ry, float _rz)
	{
		rx = _rx;
		ry = _ry;
		rz = _rz;
		fillMesh(); // update the triangle mesh
	}
	
	public void set_m(int _m)
	{
		m = _m;
		initMesh(); // resized the mesh, must re-initialize
	}
	
	public void set_n(int _n)
	{
		n = _n;
		initMesh(); // resized the mesh, must re-initialize
	}
	
	public int get_n()
	{
		return n;
	}
	
	public int get_m()
	{
		return m;
	}

	private void initMesh()
	{
		mesh = new Mesh3D(m,n);
		fillMesh();  // set the mesh vertices and normals
	}
	
	public void translate (float _xOffset, float _yOffset, float _zOffset){
		center.x += _xOffset;
		center.y += _yOffset;
		center.z += _zOffset;
	}
	
	public void rotate(Quaternion p){
		Q = p.multiply(Q);
		Q.normalize();
	}
	
	public void reset(){
		fillMesh();
		mesh.rotateMesh(Q, center);
	}
	
	// fill the triangle mesh vertices and normals
	// using the current parameters for the sphere
	private void fillMesh()
	{
		int i,j;		
		float theta, phi;
		float d_theta=(float)(2.0*Math.PI)/ ((float)(m-1));
		float d_phi=(float)Math.PI / ((float)n-1);
		float c_theta,s_theta;
		float c_phi, s_phi;
		
		for(i=0,theta=-(float)Math.PI;i<m;++i,theta += d_theta)
	    {
			c_theta=(float)Math.cos(theta);
			s_theta=(float)Math.sin(theta);
			
			for(j=0,phi=(float)(-0.5*Math.PI);j<n;++j,phi += d_phi)
			{
				// vertex location
				c_phi = (float)Math.cos(phi);
				s_phi = (float)Math.sin(phi);
				mesh.v[i][j].x=center.x+rx*c_phi*c_theta;
				mesh.v[i][j].y=center.y+ry*c_phi*s_theta;
				mesh.v[i][j].z=center.z+rz*s_phi;
				
				// unit normal to sphere at this vertex (dS/dphi cross dS/dtheta where S is the parametric equation, then normalize)
				mesh.n[i][j].x = c_phi*c_theta;
				mesh.n[i][j].y = c_phi*s_theta;
				mesh.n[i][j].z=s_phi;
			}
	    }
	}
}