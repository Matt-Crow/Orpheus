package entities;

/**
 *
 * @author Matt
 */
public class UseActiveCommand extends AbstractPlayerControlCommand{
    private final int activeNum;
    
    public static final int USE_MELEE = -1;
    
    private UseActiveCommand(int num){
        super();
        activeNum = num;
    }
    
    public static final UseActiveCommand USE_ACTIVE_CMD(int num){
        return new UseActiveCommand(num);
    }
    public static final UseActiveCommand USE_MELEE_CMD(){
        return new UseActiveCommand(USE_MELEE);
    }
    
    @Override
    public void execute(AbstractPlayer target) {
        if(activeNum == USE_MELEE){
            target.useMeleeAttack();
        } else {
            if(target instanceof HumanPlayer){
                ((HumanPlayer)target).useAttack(activeNum);
            } else {
                throw new IllegalArgumentException("Cannot use non-melee actives for non-human entities");
            }
        }
    }

}
