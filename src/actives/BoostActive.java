package actives;
import PsuedoJson.PsuedoJsonObject;
import statuses.StatusName;
import statuses.StatusTable;

public class BoostActive extends AbstractActive{
	public BoostActive(String n, StatusName[] statusNames, int[] intensities, int[] durations){
		super(ActiveType.BOOST, n, 0, 0, 0, 0, 0);
		for(int s = 0; s < statusNames.length; s++){
			addStatus(statusNames[s], intensities[s], durations[s], 100);
		}
	}
	
	public BoostActive copy(){
		BoostActive ret;
        StatusTable orig = getInflict();
		StatusName[] sn = new StatusName[orig.getSize()];
        
        
        
		int[] ints = new int[orig.getSize()];
		int[] durs = new int[orig.getSize()];
		
		for(int i = 0; i < orig.getSize(); i++){
			sn[i] = orig.getNameAt(i);
			ints[i] = orig.getIntensityAt(i);
			durs[i] = orig.getDurationAt(i);
		}
		ret = new BoostActive(getName(), sn, ints, durs);
        copyTagsTo(ret);
        return ret;
	}
	public void use(){
		super.use();
		StatusTable s = getInflict();
		for(int i = 0; i < s.getSize(); i++){
			getRegisteredTo().inflict(s.getNameAt(i), s.getIntensityAt(i), s.getDurationAt(i));
		}
	}
	public String getDescription(){
		String desc = getName() + ": \n";
		desc += "Upon use, inflicts the user with: \n";
		desc += getInflict().getStatusString();
		return desc;
	}

    @Override
    public PsuedoJsonObject getPsuedoJson() {
        PsuedoJsonObject j = new PsuedoJsonObject(getName());
        
        j.addPair("Type", this.getType().toString());
        
        
        StatusTable s = getInflict();
        PsuedoJsonObject status = null;
        for(int i = 0; i < s.getSize(); i++){
            status = new PsuedoJsonObject(s.getNameAt(i).toString());
            status.addPair("Intensity", s.getIntensityAt(i) + "");
            status.addPair("Duration", s.getDurationAt(i) + "");
            status.addPair("Chance", s.getChanceAt(i) + "");
            j.addPair(s.getNameAt(i).toString(), status);
        }
        j.addPair("Tags", getTagPsuedoJson());
        
        return j;
    }
}
