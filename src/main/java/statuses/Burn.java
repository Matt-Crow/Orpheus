package statuses;

import actions.OnUpdateEvent;
import actions.OnUpdateListener;
import controllers.Master;
import entities.AbstractPlayer;
import java.util.function.UnaryOperator;

/**
 *
 * @author Matt
 */
public class Burn  extends AbstractStatus implements OnUpdateListener{
    private static final UnaryOperator<Integer> CALC = (i)->{return Master.seconds(util.Number.minMax(1, i, 3) * 2 + 1);};
    
    public Burn(int lv, int uses){
        super(StatusName.BURN, lv, uses, CALC);
    }
    
    @Override
	public void inflictOn(AbstractPlayer p){
		p.getActionRegister().addOnUpdate(this);
	}
    
    @Override
	public String getDesc(){
		return "Burn, increasing the speed of damage taken by " + (25 * getIntensityLevel()) + "% for the next " + getMaxUses() + " seconds";
	}
    
    @Override
    public AbstractStatus copy() {
        return new Burn(getIntensityLevel(), getBaseParam());
    }
    
    @Override
    public void trigger(OnUpdateEvent t) {
        ((AbstractPlayer)t.getUpdated()).getLog().applyFilter(1 + 0.25 * getIntensityLevel());
        use();
    }
}
