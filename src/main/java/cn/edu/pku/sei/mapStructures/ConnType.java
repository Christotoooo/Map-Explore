package cn.edu.pku.sei.mapStructures;

public enum ConnType {
    
    hwy4(110),
    hwy2(130),
    dirt(143),
    river(185),
    wall(900), 
    mountain(1200),
    path(0), 
    bridge(130),	//group5 update bridge
	runway(130),    //group 3 updated airport runway
    kDefault(195);

    private int weight = 0;
    
    private ConnType(int w){
        weight = w;
    }
    public int getWeight() {
        return weight;
    }
    public boolean isBarrier() {	// Bug report!!! The mountains are barriers too! //fixed
        return this== wall || this==mountain; 	
    }  
    
    public String toTxt() {
    	String s = "";
    	switch(this) {
    	case hwy4:
    		s = "dividedhwy";
    		break;
    	case hwy2:
    		s = "hwy";
    		break;
    	case dirt:
    		s = "unpaved";
    		break;
    	case river:
    		s = "river";
    		break;
    	case wall:
    		s = "barrierwall";
    		break;
    	case mountain:
    		s = "mountain";
    		break;
    	case path:
    		s = "testpath";
    		break;
    	case bridge:
    		s = "bridge";
    		break;
    	case runway:
    		s = "runway";
    		break;
    	case kDefault:
    		s = "default";
    		break;
    	default:
    		break;
    	}
    	return s;
    }
}
