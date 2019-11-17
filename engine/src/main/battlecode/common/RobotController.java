package battlecode.common;
import java.util.ArrayList;

/**
 * A RobotController allows contestants to make their robot sense and interact
 * with the game world. When a contestant's <code>RobotPlayer</code> is
 * constructed, it is passed an instance of <code>RobotController</code> that
 * controls the newly created robot.
 */
@SuppressWarnings("unused")
public strictfp interface RobotController {

    // *********************************
    // ****** GLOBAL QUERY METHODS *****
    // *********************************

    /**
     * Returns the number of rounds in the game. After this many rounds, if neither
     * team has been destroyed, tiebreakers will be used.
     *
     * @return the number of rounds in the game.
     *
     * @battlecode.doc.costlymethod
     */
    int getRoundLimit();

    /**
     * Returns the current round number, where round 0 is the first round of the
     * match.
     *
     * @return the current round number, where round 0 is the first round of the
     * match.
     *
     * @battlecode.doc.costlymethod
     */
    int getRoundNum();

    /**
     * Returns the team's total victory points.
     *
     * @return the team's total victory points.
     *
     * @battlecode.doc.costlymethod
     */
    int getTeamSoup();

    /**
     * Returns the number of robots on your team, including your archons.
     * If this number ever reaches zero, the opposing team will automatically
     * win by destruction.
     *
     * @return the number of robots on your team, including your archons.
     *
     * @battlecode.doc.costlymethod
     */
    int getRobotCount();

    /**
     * Returns a list of the INITIAL locations of the archons of a particular
     * team. The locations will be sorted by increasing x, with ties broken by
     * increasing y. Will return an empty list if you query for {@code Team.NEUTRAL}.
     *
     * @param t the team for which you want to query the initial archon
     * locations. Will return an empty list if you query for Team.NEUTRAL
     * @return a list of the INITIAL locations of the archons of that team, or
     * an empty list for Team.NEUTRAL.
     *
     * @battlecode.doc.costlymethod
     */
    // MapLocation[] getInitialArchonLocations(Team t);

    // *********************************
    // ****** UNIT QUERY METHODS *******
    // *********************************

    /**
     * Returns the ID of this robot.
     *
     * @return the ID of this robot.
     *
     * @battlecode.doc.costlymethod
     */
    int getID();

    /**
     * Returns this robot's Team.
     *
     * @return this robot's Team.
     *
     * @battlecode.doc.costlymethod
     */
    Team getTeam();

    /**
     * Returns this robot's type (SOLDIER, ARCHON, etc.).
     *
     * @return this robot's type.
     *
     * @battlecode.doc.costlymethod
     */
    RobotType getType();

    /**
     * Returns this robot's current location.
     *
     * @return this robot's current location.
     *
     * @battlecode.doc.costlymethod
     */
    MapLocation getLocation();

    /**
     * Returns the amount of soup this robot is carrying.
     *
     * @return the amount of soup this robot is carrying.
     *
     * @battlecode.doc.costlymethod
     */
    int getSoupCarrying();

    /**
     * Returns the amount of dirt this robot is carrying.
     *
     * @return the amount of dirt this robot is carrying.
     *
     * @battlecode.doc.costlymethod
     */
    int getDirtCarrying();

    /**
     * Returns whether the robot is currently holding a unit
     *
     * @return true if the robot is currently holding another unit, false otherwise
     *
     * @battlecode.doc.costlymethod
     */
    public boolean isCurrentlyHoldingUnit();

    // ***********************************
    // ****** GENERAL SENSOR METHODS *****
    // ***********************************

    /**
     * Senses whether a MapLocation is on the map. // Will throw an exception if
     * the location is not currently within sensor range.
     *
     * @param loc the location to check
     * @return true if the location is on the map; false otherwise.
     * // @throws GameActionException if the location is not within sensor range.
     *
     * @battlecode.doc.costlymethod
     */
    boolean onTheMap(MapLocation loc);

    /**
     * Senses whether the given location is within the robot's sensor range.
     *
     * @param loc the location to check
     * @return true if the given location is within the robot's sensor range; false otherwise.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canSenseLocation(MapLocation loc);

    /**
     * Senses whether a point at the given radius is within the robot's sensor range.
     *
     * @param radius the radius to check
     * @return true if the given location is within the robot's sensor range; false otherwise.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canSenseRadius(int radius);

    /**
     * Senses whether there is a robot at the given location.
     *
     * @param loc the location to check
     * @return true if there is a robot at the given location; false otherwise.
     * @throws GameActionException if the location is not within sensor range.
     *
     * @battlecode.doc.costlymethod
     */
    boolean isLocationOccupied(MapLocation loc) throws GameActionException;

    /**
     * Senses the robot at the given location, or null if there is no robot
     * there.
     *
     * @param loc the location to check
     * @return the robot at the given location.
     * @throws GameActionException if the location is not within sensor range.
     *
     * @battlecode.doc.costlymethod
     */
    RobotInfo senseRobotAtLocation(MapLocation loc) throws GameActionException;

    /**
     * Tests whether the given robot exists and any part of the given robot is
     * within this robot's sensor range.
     *
     * @param id the ID of the robot to query
     * @return true if the given robot is within this robot's sensor range; false otherwise.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canSenseRobot(int id);

    /**
     * Senses information about a particular robot given its ID.
     *
     * @param id the ID of the robot to query
     * @return a RobotInfo object for the sensed robot.
     * @throws GameActionException if the robot cannot be sensed (for example,
     * if it doesn't exist or is out of sight range).
     *
     * @battlecode.doc.costlymethod
     */
    RobotInfo senseRobot(int id) throws GameActionException;

    /**
     * Returns all robots within sense radius. The objects are returned in order of
     * increasing distance from your robot.
     *
     * @return sorted array of RobotInfo objects, which contain information about all
     * the robots you sensed.
     *
     * @battlecode.doc.costlymethod
     */
    RobotInfo[] senseNearbyRobots();

    /**
     * Returns all robots that can be sensed within a certain radius of this
     * robot. The objects are returned in order of increasing distance from
     * your robot.
     *
     * @param radius return robots this distance away from the center of
     * this robot. If -1 is passed, all robots within sense radius are returned.
     * @return sorted array of RobotInfo objects of all the robots you sensed.
     *
     * @battlecode.doc.costlymethod
     */
    RobotInfo[] senseNearbyRobots(int radius);

    /**
     * Returns all robots of a given team that can be sensed within a certain
     * radius of this robot. The objects are returned in order of increasing distance
     * from your robot.
     *
     * @param radius return robots this distance away from the center of
     * this robot. If -1 is passed, all robots within sense radius are returned
     * @param team filter game objects by the given team. If null is passed,
     * robots from any team are returned
     * @return sorted array of RobotInfo objects of all the robots you sensed.
     *
     * @battlecode.doc.costlymethod
     */
    RobotInfo[] senseNearbyRobots(int radius, Team team);

    /**
     * Returns all robots of a given team that can be sensed within a certain
     * radius of a specified location. The objects are returned in order of
     * increasing distance from the specified center.
     *
     * @param center center of the given search radius
     * @param radius return robots this distance away from the given center
     * location. If -1 is passed, all robots within sense radius are returned
     * @param team filter game objects by the given team. If null is passed,
     * objects from all teams are returned
     * @return sorted array of RobotInfo objects of the robots you sensed.
     *
     * @battlecode.doc.costlymethod
     */
    RobotInfo[] senseNearbyRobots(MapLocation center, int radius, Team team);

    /**
     * Returns the pollution level at a given location, if the location is
     * within the sensor radius of the robot.
     *
     * @param loc the given location
     * @return the pollution level at a given location, if the location is
     * within the sensor radius of the robot.
     *
     * @battlecode.doc.costlymethod
     */
    int sensePollution(MapLocation loc) throws GameActionException;

    /**
     * Returns the location adjacent to current location in the given direction.
     *
     * @param dir the given direction
     * @return the location adjacent to current location in the given direction.
     *
     * @battlecode.doc.costlymethod
     */
    MapLocation adjacentLocation(Direction dir);

    // ***********************************
    // ****** READINESS METHODS **********
    // ***********************************
    
    /**
     * Returns whether the robot's action cooldown has expired.
     * 
     * @return true if the robot's action cooldown has expired; false otherwise.
     *
     * @battlecode.doc.costlymethod
     */
    boolean isReady();

    /**
     * Returns the number of cooldown turns remaining before this unit can act again.
     * When this number is 0, isReady() is true.
     *
     * @return the number of cooldown turns remaining before this unit can act again.
     *
     * @battlecode.doc.costlymethod
     */
    int getCooldownTurns();

    // ***********************************
    // ****** MOVEMENT METHODS ***********
    // ***********************************

    /**
     * Tells whether this robot can move one stride in the given direction,
     * without taking into account if they have already moved. Takes into account only
     * the positions of other robots, and the edge of the
     * game map. Does not take into account whether this robot is currently
     * active. Note that one stride is equivalent to this robot's {@code strideRadius}.
     *
     * @param dir the direction to move in
     * @return true if there is no external obstruction to prevent this robot
     * from moving one stride in the given direction; false otherwise.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canMove(Direction dir);
    
    /**
     * Tests whether this robot can move to the target MapLocation. If
     * the location is outside the robot's {@code strideRadius}, the location
     * is rescaled to be at the {@code strideRadius}. Takes into account only
     * the positions of other robots, and the edge of the game map. Does
     * not take into account whether this robot is currently active.
     * 
     * @param center the MapLocation to move to
     * @return true if there is no external obstruction to prevent this robot
     * from moving to this MapLocation (or in the direction of this MapLocation
     * if it is too far); false otherwise.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canMove(MapLocation center);
    
    /**
     * Moves one stride in the given direction. Note that one stride is equivalent
     * to this robot's {@code strideRadius}.
     *
     * @param dir the direction to move in
     * @throws GameActionException if the robot cannot move one stride in this
     * direction, such as already moved that turn, the target location being
     * off the map, and the target destination being occupied with either
     * another robot.
     *
     * @battlecode.doc.costlymethod
     */
    void move(Direction dir) throws GameActionException;

    // ***********************************
    // ****** BUILDING/SPAWNING **********
    // ***********************************

    /**
     * Tests whether you have the dependencies to build the given
     * robot, and this robot is a valid builder for the target robot.
     *
     * @param type the type of robot to build
     * @return true if the requirements to build the given robot are met; false otherwise.
     *
     * @battlecode.doc.costlymethod
     */
    boolean hasRobotBuildRequirements(RobotType type);

    /**
     * Tests whether the robot can build a robot of the given type in the
     * given direction. Checks cooldown turns remaining,
     * whether the robot can build, and that the given direction is
     * not blocked.
     *
     * @param dir the direction to build in
     * @param type the type of robot to build
     * @return whether it is possible to build a robot of the given type in the
     * given direction.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canBuildRobot(RobotType type, Direction dir);

    /**
     * Builds a robot of the given type in the given direction.
     *
     * @param dir the direction to spawn the unit
     * @param type the type of robot to build
     * @throws GameActionException if you don't have enough currency, if
     * the robot is still in build cooldown, if the direction is not a
     * good build direction, or if this robot is not of an appropriate type.
     *
     * @battlecode.doc.costlymethod
     */
    void buildRobot(RobotType type, Direction dir) throws GameActionException;

    // ***********************************
    // ****** MINER METHODS **************
    // ***********************************

    /**
     * Tests whether the robot can mine soup in the given direction.
     * Checks cooldown turns remaining, whether the robot can mine,
     * and that the given direction has soup.
     *
     * @param dir the direction to mine
     * @return whether it is possible to mine soup in the given direction.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canMineSoup(Direction dir);

    /**
     * Mines soup in the given direction.
     *
     * @param dir the direction to mine
     * @throws GameActionException if this robot is not a miner, if
     * the robot is still in cooldown, or if there is no soup to mine.
     *
     * @battlecode.doc.costlymethod
     */
    void mineSoup(Direction dir) throws GameActionException;

    /**
     * Tests whether the robot can refine soup in the given direction.
     * Checks cooldown turns remaining, whether the robot can refine, whether
     * the robot has crude soup, and that the given direction has a refinery.
     *
     * @param dir the direction to refine
     * @return whether it is possible to refine soup in the given direction.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canRefineSoup(Direction dir);

    /**
     * Refines soup in the given direction (max up to specified amount).
     *
     * @param dir the direction to refine
     * @param amount the amount of soup to refine
     * @throws GameActionException if this robot is not a miner, if
     * the robot is still in cooldown, if there is no soup to refine,
     * or if there is no refinery.
     *
     * @battlecode.doc.costlymethod
     */
    void refineSoup(Direction dir, int amount) throws GameActionException;

    /**
     * Tests whether a robot is able to pick up a specific unit.
     *
     * @param id the id of the robot to pick up
     * @return true if id can be picked up by the robot, false otherwise
     */
    boolean canPickUpUnit(int id);

    /**
     * Picks up another unit.
     *
     * @throws GameActionException if the robot is not of type DELIVERY_DRONE or cannot pick up
     * a unit because it is already carrying a unit or if the unit to pick up is not in the radius
     * of this robot.
     */
    void pickUpUnit(int id) throws GameActionException;

    /**
     * Tests whether a robot is able to drop a unit in a specified direction.
     *
     * @param dir the specified direction
     * @return true if a robot can be dropped off, false otherwise
     */
    boolean canDropUnit(Direction dir);

    /**
     * Drops the unit that is currently picked up.
     *
     * @throws GameActionException if the robot is not of type DELIVERY_DRONE or if the robot is not currently
     * holding a unit that it can drop.
     */
    void dropUnit(Direction dir) throws GameActionException;

    // ***************************************
    // ********* LANDSCAPER METHODS **********
    // ***************************************

    /**
     * Tests whether the robot can dig dirt in the given direction.
     * Checks cooldown turns remaining, whether the robot can dig,
     * that the robot can carry more dirt, and that the location is valid.
     *
     * @param dir the direction to dig in
     * @return whether it is possible to dig dirt from the given direction.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canDigDirt(Direction dir);

    /**
     * Digs dirt in the given direction.
     *
     * @param dir the direction to dig in
     * @throws GameActionException if this robot is not a landscaper, if
     * the robot is still in cooldown, the robot cannot carry more dirt,
     * or the location is not valid (there is a robot/building).
     *
     * @battlecode.doc.costlymethod
     */
    void digDirt(Direction dir) throws GameActionException;

    /**
     * Tests whether the robot can deposit dirt in the given direction.
     * Checks cooldown turns remaining, whether the robot can deposit dirt,
     * and whether the robot has dirt.
     *
     * @param dir the direction to deposit
     * @return whether it is possible to deposit dirt in the given direction.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canDepositDirt(Direction dir);

    /**
     * Deposits dirt in the given direction (max up to specified amount).
     *
     * @param dir the direction to deposit
     * @throws GameActionException if this robot is not a landscaper, if
     * the robot is still in cooldown, if there is no dirt to deposit,
     *
     * @battlecode.doc.costlymethod
     */
    void depositDirt(Direction dir) throws GameActionException;

    // ***********************************
    // ****** BLOCKCHAINNNNNNNNNNN *******
    // ***********************************

    /**
     * Sends a message to the blockchain at the indicated cost.
     * 
     * @param messageArray the list of ints to send. if more than K messages, 
     * @param cost the price that the unit is willing to pay for the message
     * 
     */
    public void sendMessage(int[] messageArray, int cost) throws GameActionException;


    /**
     * Gets all messages that were sent at a given round.
     * 
     * Because one of me and java is stupid, this is returned as a string where a space
     * separates messages and an underscore separates the numbers in messages.
     * 
     * @param roundNumber the round index.
     * @throws GameActionException
     */
    public String getRoundMessages(int roundNumber) throws GameActionException;

    // ***********************************
    // ****** OTHER ACTION METHODS *******
    // ***********************************

    /**
     * Kills your robot and ends the current round. Never fails.
     *
     * @battlecode.doc.costlymethod
     */
    void disintegrate();

    /**
     * Causes your team to lose the game. It's like typing "gg."
     *
     * @battlecode.doc.costlymethod
     */
    void resign();

    // ***********************************
    // **** INDICATOR STRING METHODS *****
    // ***********************************

    /**
     * Draw a dot on the game map for debugging purposes.
     *
     * @param loc the location to draw the dot.
     * @param red the red component of the dot's color.
     * @param green the green component of the dot's color.
     * @param blue the blue component of the dot's color.
     *
     * @battlecode.doc.costlymethod
     */
    void setIndicatorDot(MapLocation loc, int red, int green, int blue);

    /**
     * Draw a line on the game map for debugging purposes.
     *
     * @param startLoc the location to draw the line from.
     * @param endLoc the location to draw the line to.
     * @param red the red component of the line's color.
     * @param green the green component of the line's color.
     * @param blue the blue component of the line's color.
     *
     * @battlecode.doc.costlymethod
     */
    void setIndicatorLine(MapLocation startLoc, MapLocation endLoc, int red, int green, int blue);

    // ***********************************
    // ******** DEBUG METHODS ************
    // ***********************************

    /**
     * Gets this robot's 'control bits' for debugging purposes. These bits can
     * be set manually by the user, so a robot can respond to them. To set these
     * bits, you must run the client in lockstep mode and right click the
     * units.
     *
     * @return this robot's control bits.
     *
     * @battlecode.doc.costlymethod
     */
    long getControlBits();
}
