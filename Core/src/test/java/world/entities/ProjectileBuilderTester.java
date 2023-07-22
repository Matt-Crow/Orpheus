package world.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import orpheus.core.world.occupants.players.Player;
import world.WorldBuilderImpl;
import world.battle.Team;

public class ProjectileBuilderTester {
    
    @Test
    public void at_givenPlayer_spawnsAtTheirLocation() {
        var t = Team.ofPlayers();
        var world = new WorldBuilderImpl()
            .withPlayers(t)
            .build();
        var player = Player.makeDrone(world, 1);
        player.setX(42);
        player.setY(64);
        player.setFacing(123);
        t.addMember(player);
        
        var builder = new ProjectileBuilder()
            .havingHitNoPlayersYet()
            .withUser(player);
        
        builder = builder.at(player);
        var actual = builder.build();

        Assertions.assertEquals(player.getX(), actual.getX());
        Assertions.assertEquals(player.getY(), actual.getY());
        Assertions.assertEquals(player.getFacing(), actual.getFacing());
    }

    @Test
    public void doubleHitting_doesNotExist() {
        var world = new WorldBuilderImpl()
            .build();
        
        var player = Player.makeDrone(world, 1);
        var spy = new HitListenerSpy();
        player.getActionRegister().addOnBeHit(spy);
        world.getPlayers().addMember(player);
        
        var attacker = Player.makeDrone(world, 1);
        world.getAi().addMember(attacker);
        var builder = new ProjectileBuilder()
            .havingHitNoPlayersYet()
            .withUser(attacker)
            .at(player);
        
        var first = builder.build();
        var second = builder.build();
        first.hitIfColliding(player);
        second.hitIfColliding(player);

        Assertions.assertEquals(1, spy.getTimesCalled());
    }
}
