package battlecode.world;

import battlecode.common.BuilderController;
import battlecode.common.Chassis;
import battlecode.common.ComponentType;
import battlecode.common.GameActionException;
import battlecode.common.GameActionExceptionType;
import battlecode.common.MapLocation;
import battlecode.common.RobotLevel;

import battlecode.world.signal.EquipSignal;
import battlecode.world.signal.SpawnSignal;

public class Builder extends BaseComponent implements BuilderController {

	public Builder(ComponentType type, InternalRobot robot) {
		super(type,robot);
	}

	public void build(ComponentType type, MapLocation loc, RobotLevel level) throws GameActionException {
		assertNotNull(level);
		assertNotNull(type);
		assertCanBuild(type);
		assertInactive();
		assertWithinRange(loc);
		InternalRobot ir = alliedRobotAt(loc,level);
		spendResources(type.cost);
		activate(new EquipSignal(robot,ir,type));
	}

	public void build(Chassis type, MapLocation loc) throws GameActionException {
		assertNotNull(type);
		assertCanBuild(type);
		assertInactive();
		assertWithinRange(loc);
		if(!gameWorld.canMove(type.level,loc))
			throw new GameActionException(GameActionExceptionType.CANT_MOVE_THERE,"That square is occupied.");
		spendResources(type.cost);
		activate(new SpawnSignal(loc,type,robot.getTeam(),robot));
	}

	public void assertCanBuild(ComponentType type) {
	}

	public void assertCanBuild(Chassis type) {
	}

}
