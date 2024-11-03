package csci.ooad.polymorphia;

import csci.ooad.polymorphia.characters.Adventurer;
import csci.ooad.polymorphia.characters.Creature;
import csci.ooad.polymorphia.characters.Character;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.*;


public class Room {
    private final String name;
    private final List<Room> neighbors = new ArrayList<>();
    private final List<Character> characters = new ArrayList<>();
    private final List<Food> foodItems = new ArrayList<>();

    public Room(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Adventurer> getLivingAdventurers() {
        return characters.stream()
                .filter(Character::isAdventurer)
                .filter(Character::isAlive)
                .map(Adventurer.class::cast)
                .sorted()
                .toList();
    }

    public List<Creature> getLivingCreatures() {
        return characters.stream()
                .filter(Character::isCreature)
                .filter(Character::isAlive)
                .map(Creature.class::cast)
                .sorted()
                .toList();
    }

    public List<String> getContents() {
        List<String> contents = new ArrayList<>(getLivingCharacters().stream()
                .map(Object::toString)
                .toList());
        contents.addAll(this.foodItems.stream()
                .map(Object::toString)
                .toList());
        return unmodifiableList(contents);
    }

    private void addNeighbor(Room neighbor) {
        // Make sure we are never a neighbor of ourselves
        assert this != neighbor;
        this.neighbors.add(neighbor);
    }

    public void connect(Room neighbor) {
        this.addNeighbor(neighbor);
        neighbor.addNeighbor(this);
    }

    @Override
    public String toString() {
        String representation = "\t" + name + ":\n\t\t";
        representation += String.join("\n\t\t", getContents());
        return representation;
    }

    public void add(Character character) {
        characters.add(character);
        character.enterRoom(this);
    }

    public Boolean hasLivingCreatures() {
        return characters.stream()
                .filter(Character::isCreature)
                .filter(Character::isAlive)
                .anyMatch(Character::isAlive);
    }

    public Boolean hasLivingAdventurers() {
        return characters.stream()
                .filter(Character::isAdventurer)
                .filter(Character::isAlive)
                .anyMatch(Character::isAlive);
    }

    public void remove(Character character) {
        characters.remove(character);
    }

    public Room getRandomNeighbor() {
        if (neighbors.isEmpty()) {
            return null;
        }
        return neighbors.get(Die.randomLessThan(neighbors.size()));
    }

    public void enter(Character character) {
        Room currentRoom = character.getCurrentLocation();
        currentRoom.remove(character);
        this.add(character);
    }

    public List<Character> getLivingCharacters() {
        return characters.stream()
                .filter(Character::isAlive)
                .toList();
    }

    public void add(Food foodItem) {
        foodItems.add(foodItem);
    }

    public Adventurer getHealthiestAdventurer() {
        return getLivingAdventurers().stream().max(Comparator.naturalOrder()).get();
    }

    public Creature getHealthiestCreature() {
        return getLivingCreatures().stream().max(Comparator.naturalOrder()).get();
    }

    public boolean hasFood() {
        return !foodItems.isEmpty();
    }

    public Food removeFoodItem() throws NoFoodException {
        if (foodItems.isEmpty()) {
            throw new NoFoodException("No food in room");
        }
        return foodItems.removeFirst();
    }

    public Boolean hasDemon() {
        return characters.stream().filter(Character::isAlive).anyMatch(Character::isDemon);
    }

    public int numberOfNeighbors() {
        return neighbors.size();
    }

    public boolean hasNeighbor(Room neighbor) {
        return neighbors.contains(neighbor);
    }

    public boolean hasCoward() {
        return characters.stream().filter(Character::isAlive).anyMatch(Character::isCoward);
    }

    public List<Room> getNeighbors() {
        return List.copyOf(neighbors);
    }

    public Character getDemon() {
        return characters.stream().filter(Character::isAlive).filter(Character::isDemon).findFirst().get();
    }
}
