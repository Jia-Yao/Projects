//****************************************************************************
//      CappedCylinder class
//****************************************************************************
// History :
//   Dec 6, 2015 Created by Jia Yao
//

public class CappedCylinder3D
{
	private Vector3D center, tmp = new Vector3D();
	public float r, height;
	private int m,n;
	public Mesh3D mesh;
	public Mesh3D end_mesh;		// the triangle mesh of the two ends
	private Quaternion Q = new Quaternion();
	
	public CappedCylinder3D(float _x, float _y, float _z, float _r, float _h, int _m, int _n)
	{
		center = new Vector3D(_x,_y,_z);
		r = _r;
		height = _h;
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
	
	public void set_radius(float _r)
	{
		r = _r;
		fillMesh(); // update the triangle mesh
	}
	
	public void set_height(float _h)
	{
		height = _h;
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
		end_mesh = new Mesh3D(m+1,2);	
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
		tmp.set(center.x, center.y, center.z+(float)height/2);
		mesh.rotateMesh(Q, tmp);
		end_mesh.rotateMesh(Q, tmp);
	}
	
	// fill the triangle mesh vertices and normals
	// using the current parameters for the cylinder
	private void fillMesh()
	{
		int i,j;		
		float theta, u;
		float d_theta=(float)(2.0*Math.PI)/ ((float)(m-1));
		float d_u= height / ((float)n-1);
		float c_theta,s_theta;
		
		for(i=0,theta=-(float)Math.PI;i<m;++i,theta += d_theta)
	    {
			c_theta=(float)Math.cos(theta);
			s_theta=(float)Math.sin(theta);
			
			for(j=0,u=0;j<n;++j,u += d_u)
			{
				// vertex location
				mesh.v[i][j].x=center.x+r*c_theta;
				mesh.v[i][j].y=center.y+r*s_theta;
				mesh.v[i][j].z=center.z+u;
				
				// unit normal to cylinder at this vertex (dS/du cross dS/dtheta where S is the parametric equation, then normalize)
				mesh.n[i][j].x = c_theta;
				mesh.n[i][j].y = s_theta;
				mesh.n[i][j].z = 0;
				
				// the two ends vertex location and unit normal
				if (j == 0){
					end_mesh.v[i+1][0].x = center.x+r*c_theta;
					end_mesh.v[i+1][0].y = center.y+r*s_theta;
					end_mesh.v[i+1][0].z = center.z+u;
					end_mesh.n[i+1][0].x = 0;
					end_mesh.n[i+1][0].y = 0;
					end_mesh.n[i+1][0].z = -1;
				}
				if (j == n-1){
					end_mesh.v[i+1][1].x = center.x+r*c_theta;
					end_mesh.v[i+1][1].y = center.y+r*s_theta;
					end_mesh.v[i+1][1].z = center.z+u;
					end_mesh.n[i+1][1].x = 0;
					end_mesh.n[i+1][1].y = 0;
					end_mesh.n[i+1][1].z = 1;
				}	
			}
	    }
		
		end_mesh.v[0][0].x = center.x;
		end_mesh.v[0][0].y = center.y;
		end_mesh.v[0][0].z = center.z;
		
		end_mesh.n[0][0].x = 0;
		end_mesh.n[0][0].y = 0;
		end_mesh.n[0][0].z = -1;
		
		end_mesh.v[0][1].x = center.x;
		end_mesh.v[0][1].y = center.y;
		end_mesh.v[0][1].z = center.z+height;
		
		end_mesh.n[0][1].x = 0;
		end_mesh.n[0][1].y = 0;
		end_mesh.n[0][1].z = 1;
	}
}