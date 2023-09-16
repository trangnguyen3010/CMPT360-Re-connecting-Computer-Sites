package lib280.base;

import java.io.IOException;

public interface GraphvizRepresentation280 {
	/**
	 * Create/overwrite the given filename as an ASCII file
	 * containing a Graphviz dot script to generate a graphical
	 * representation of the implementing data structure. 
	 * @param filename Name of the file to create/overwrite
	 * @throws IOException
	 */
	public void writeGraphviz_dot(String filename) throws IOException;
}
