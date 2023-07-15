package world.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import world.WorldBuilderImpl;
import world.battle.Team;

public class ProjectileBuilderTester {
    
    @Test
    public void at_givenPlayer_spawnsAtTheirLocation() {
        var t = Team.ofPlayers();
        var world = new WorldBuilderImpl()
            .withPlayers(t)
            .build();
        var player = AbstractPlayer.makeDrone(world, 1);
        player.setX(42);
        player.setY(64);
        player.setFacing(123);
        t.addMember(player);
        
        var builder = new ProjectileBuilder()
            .withNewUseId()
            .withMomentum(71)
            .withUser(player);
        
        builder = builder.at(player);
        var actual = builder.build();

        Assertions.assertEquals(player.getX(), actual.getX());
        Assertions.assertEquals(player.getY(), actual.getY());
        Assertions.assertEquals(player.getFacing(), actual.getFacing());
    }
}
