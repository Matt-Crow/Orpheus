package commands;

import util.CardinalDirection;
import world.World;
import world.entities.HumanPlayer;

/**
 * probably temporary
 * @author Matt Crow
 */
public class ControlDecoder {

    public static void decode(World world, String s) {
        //Scanner tokenizer = new Scanner(s);
        int[] coords;
        HumanPlayer p = null;
        //String st = tokenizer.findInLine("#.*\b");
        if (s.contains("#")) {
            // contains player id
            int startIndex = s.indexOf("#") + 1;
            int endIndex = s.indexOf(" ", startIndex);
            String playerId = s.substring(startIndex, endIndex);
            p = (HumanPlayer) world.getPlayers().getMemberById(playerId);
        } else {
            throw new UnsupportedOperationException("cannot decode string with no player id");
        }
        for (String str : s.split("\n")) {
            if (str.contains("turn to")) {
                coords = decodeMouseString(str);
                p.turnTo(coords[0], coords[1]);
            }
            if (str.contains("move to")) {
                coords = decodeMouseString(str);
                p.setPath(coords[0], coords[1]);
            }
            if (str.contains("use melee")) {
                p.useMeleeAttack();
            } else if (str.contains("use")) {
                int num = Integer.parseInt(str.substring(str.indexOf("use") + 3).trim());
                p.useAttack(num);
            }
            if (str.contains("start move direction")) {
                int idx = str.indexOf("start move direction") + "start move direction".length() + 1;
                String dirName = str.substring(idx);
                p.setMovingInDir(CardinalDirection.fromString(dirName), true);
            }
            if (str.contains("stop move direction")) {
                int idx = str.indexOf("stop move direction") + "stop move direction".length() + 1;
                String dirName = str.substring(idx);
                p.setMovingInDir(CardinalDirection.fromString(dirName), false);
            }
        }
    }

    public static int[] decodeMouseString(String s) {
        String coords = s.substring(s.indexOf('(') + 1, s.indexOf(')'));
        String[] split = coords.split(",");
        int x = Integer.parseInt(split[0].trim());
        int y = Integer.parseInt(split[1].trim());
        return new int[]{x, y};
    }
    
}
