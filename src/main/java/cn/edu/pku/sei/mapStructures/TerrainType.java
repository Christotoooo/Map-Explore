package cn.edu.pku.sei.mapStructures;


/**
 * 添加kCity
 * @author 李曜铮 1800940649
 *
 */
public enum TerrainType {

    //Story 57 Designed pop-up cell info contents
    kDefault(165,"Flat open land: \n Hint: Easy to traverse.\n Hey, go for a hike!"),
    
    kForest(195,"Forest: \n Hint: All trees!\n Super hard to traverse with your beloved car but you may walk and embrace the nature!"),
    kFlag1(0,"Start Point\n"),
    kWater(190,"Water: \n Hint: Shhh quiet water, watch out!\n NOT gonna try to traverse with your fancy car TAT."),
    kBrush(180,"Brush: \n Hint: Okay to traverse, but with low obstacles.\n Go explore the wild!"),
    kFlag2(0,"Target Point\n"),
    kAirport(105,"Airport: \n Hint: Airplane region, be serious dude."), //group 3 airport
    kUrban(100,"Urban area: \n Hint:  Feel free to visit and grab a beer!");     //group1 update kUrban
    //

    private int numVal;
    private String cellDescription;//group 3
    TerrainType(int val,String cellDescription){
        this.numVal = val;
        this.cellDescription = cellDescription;//group 3
    }
    public int getNumVal() {
        return numVal;
    }

    public int getWeight() {
        return numVal;
    }
    public String getDescription(){return cellDescription;}//group 3
    
    public String toTxt() {
		String s = "";
		switch (this) {
		case kAirport:
			s = "airport";
			break;
		case kBrush:
			s = "brush";
			break;
		case kFlag1:
			s = "flag1";
			break;
		case kFlag2:
			s = "flag2";
			break;
		case kForest:
			s = "forest";
			break;
		case kUrban:
			s = "urban";
			break;
		case kWater:
			s = "water";
			break;
		case kDefault:
			s = "default";
			break;
		default:
			s = "";
			break;
		}
		return s;
	}
}
