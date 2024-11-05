package csci.ooad.polymorphia;

import csci.ooad.layout.intf.IMazeObserver;
import csci.ooad.layout.intf.MazeObserver;
import csci.ooad.polymorphia.characters.Creature;
import csci.ooad.polymorphia.maze.Maze;
import csci.ooad.polymorphia.observer.AudibleObserver;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameObserverTest {

    private static final int SECONDS_TO_PAUSE_BETWEEN_TURNS = 0;

    @Test
    void bigMazeTestWithAudioObserver() {
        int numberOfRooms = 12;
        int connectionsPerRoom = 3;
        int numberOfAdventurers = 2;
        int numberOfCowards = 1;
        int numberOfKnights = 2;
        int numberOfGluttons = 1;
        int numberOfCreatures = 5;
        int numberOfDemons = 2;
        int numberOfFoodItems = 15;

        Maze maze = Maze.getNewBuilder()
                .createConnectedRooms(connectionsPerRoom, numberOfRooms)
                .createAndAddAdventurers(numberOfAdventurers)
                .createAndAddCowards(numberOfCowards)
                .createAndAddKnights(numberOfKnights)
                .createAndAddGluttons(numberOfGluttons)
                .createAndAddCreatures(numberOfCreatures)
                .createAndAddDemons(numberOfDemons)
                .createAndAddFoodItems(numberOfFoodItems)
                .build();

        Polymorphia polymorphia = new Polymorphia("bigMazeTestWithAudioObserver", maze);

        // Add observers
        AudibleObserver audibleObserver = new AudibleObserver(polymorphia, List.of(
                EventType.Death,
                EventType.GameStart,
                EventType.GameOver,
                EventType.PickedUpSomething));

        polymorphia.play();
        polymorphia.detach(audibleObserver);

        assertTrue(polymorphia.numberOfLivingAdventurers() == 0 ||
                polymorphia.numberOfLivingCreatures() == 0);
        assertTrue(polymorphia.isOver());
    }

    @Disabled
    void bigMazeTestWithMazeObserver() {
        int connectionsPerRoom = 3;
        int numberOfAdventurers = 2;
        int numberOfCowards = 1;
        int numberOfKnights = 2;
        int numberOfGluttons = 1;
        int numberOfCreatures = 3;
        int numberOfDemons = 2;
        int numberOfFoodItems = 12;

        Maze maze = Maze.getNewBuilder()
                .createConnectedRooms(
                        connectionsPerRoom,
                        "Rivendell", "Mordor", "BagEnd", "Swamp",
                        "Crystal Palace", "Pool of Lava", "Fangorn-Forest"
                )
                .createAndAddAdventurers(numberOfAdventurers)
                .createAndAddCowards(numberOfCowards)
                .createAndAddKnights(numberOfKnights)
                .createAndAddGluttons(numberOfGluttons)
                .createAndAddCreatures(numberOfCreatures)
                .createAndAddDemons(numberOfDemons)
                .createAndAddFoodItems(numberOfFoodItems)
                .build();

        Polymorphia polymorphia = new Polymorphia("bigMazeTestWithMazeObserver", maze);

        // Add observers
        IMazeObserver mazeObserver = MazeObserver.getNewBuilder(polymorphia.getName())
                .useRadialLayoutStrategy()
                .setDelayInSecondsAfterUpdate(SECONDS_TO_PAUSE_BETWEEN_TURNS)
                .setRoomDimension(250)
                .setDimension(1500)
                .build();
        polymorphia.attach(mazeObserver);

        // Act
        polymorphia.play();
        polymorphia.detach(mazeObserver);

        // Assert
        assertTrue(polymorphia.isOver());
    }

    @Test
    void testSmallMaze() {
        int connectionsPerRoom = 1;
        int numberOfAdventurers = 1;
        int numberOfCreatures = 1;

        Maze maze = Maze.getNewBuilder()
                .createConnectedRooms(
                        connectionsPerRoom,
                        "Rivendell", "Mordor"
                )
                .createAndAddAdventurers(numberOfAdventurers)
                .createAndAddCreatures(numberOfCreatures)
                .build();

        Polymorphia polymorphia = new Polymorphia("testSmallMaze", maze);

        // Add observers
        IMazeObserver mazeObserver = MazeObserver.getNewBuilder(polymorphia.getName())
                .useRadialLayoutStrategy()
                .setDelayInSecondsAfterUpdate(SECONDS_TO_PAUSE_BETWEEN_TURNS)
                .setRoomDimension(250)
                .setDimension(1500)
                .build();
        polymorphia.attach(mazeObserver);

        AudibleObserver audibleObserver = new AudibleObserver(polymorphia, List.of(
                EventType.Death,
                EventType.GameStart,
                EventType.GameOver,
                EventType.FightOutcome,
                EventType.Moved,
                EventType.AteSomething));

        // Act
        polymorphia.play();
        polymorphia.detach(audibleObserver);

        // Assert
        assertTrue(polymorphia.isOver());
    }

    @Test
    void testOneRoomMaze() {
        Maze maze = Maze.getNewBuilder()
                .createRoom("Rivendell")
                .createAndAddCowards("Sir Run Away")
                .createAndAddCreatures("Orc")
                .build();

        Polymorphia polymorphia = new Polymorphia("testOneRoomMaze", maze);

        // Add observers
        IMazeObserver mazeObserver = MazeObserver.getNewBuilder(polymorphia.getName())
                .setDelayInSecondsAfterUpdate(SECONDS_TO_PAUSE_BETWEEN_TURNS)
                .build();
        polymorphia.attach(mazeObserver);

        // Act
        polymorphia.play();
        polymorphia.detach(mazeObserver);

        // Assert
        assertTrue(polymorphia.isOver());
        if (polymorphia.hasLivingCreatures()) {
            Creature creature = polymorphia.getLivingCreatures().getFirst();
            assertTrue(creature.getHealth() < Creature.DEFAULT_INITIAL_HEALTH);
        }
    }

}
