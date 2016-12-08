import StructOfArrays from './soa';
import Metadata from './metadata';
import {schema, flatbuffers} from 'battlecode-schema';

// necessary because victor doesn't use exports.default
import Victor = require('victor');

export type BodiesSchema = {
  id: Int32Array,
  team: Int8Array,
  type: Int8Array,
  x: Float32Array,
  y: Float32Array,
  health: Float32Array,
  radius: Float32Array
};

export type BulletsSchema = {
  id: Int32Array,
  x: Float32Array,
  y: Float32Array,
  velX: Float32Array,
  velY: Float32Array,
  damage: Float32Array,
  spawnedTime: Uint16Array
};


/**
 * A frozen image of the game world.
 *
 * TODO(jhgilles): better access control on contents.
 */
export default class GameWorld {
  /**
   * Everything that isn't a bullet.
   * {
   *   id: Int32Array,
   *   team: Int8Array,
   *   type: Int8Array,
   *   x: Float32Array,
   *   y: Float32Array,
   *   health: Float32Array,
   *   radius: Float32Array
   * }
   */
  bodies: StructOfArrays<BodiesSchema>;

  /*
   * Bullets.
   * {
   *   id: Int32Array,
   *   x: Float32Array,
   *   y: Float32Array,
   *   velX: Float32Array,
   *   velY: Float32Array,
   *   damage: Float32Array,
   *   spawnedTime: Uint16Array
   * }, 'id', capacity)
   */
  bullets: StructOfArrays<BulletsSchema>;

  /**
   * The current turn.
   */
  turn: number;

  /**
   * The minimum corner of the game world.
   */
  minCorner: Victor;

  /**
   * The maximum corner of the game world.
   */
  maxCorner: Victor;

  /**
   * The name of the map.
   */
  mapName: string;

  /**
   * Metadata about the current game.
   */
  meta: Metadata;

  // Cache fields
  // We pass these into flatbuffers functions to avoid allocations, but that's
  // it, they don't hold any state
  private _bodiesSlot: schema.SpawnedBodyTable;
  private _bulletsSlot: schema.SpawnedBulletTable;
  private _vecTableSlot: schema.VecTable;

  constructor(meta: Metadata) {
    this.meta = meta;

    this.bodies = new StructOfArrays({
      id: new Int32Array(0),
      team: new Int8Array(0),
      type: new Int8Array(0),
      x: new Float32Array(0),
      y: new Float32Array(0),
      health: new Float32Array(0),
      radius: new Float32Array(0)
    }, 'id');

    this.bullets = new StructOfArrays({
      id: new Int32Array(0),
      x: new Float32Array(0),
      y: new Float32Array(0),
      velX: new Float32Array(0),
      velY: new Float32Array(0),
      spawnedTime: new Uint16Array(0),
      damage: new Float32Array(0)
    }, 'id');

    this.turn = 0;
    this.minCorner = new Victor(0, 0);
    this.maxCorner = new Victor(0, 0);
    this.mapName = '????';

    this._bodiesSlot = new schema.SpawnedBodyTable()
    this._bulletsSlot = new schema.SpawnedBulletTable()
    this._vecTableSlot = new schema.VecTable();
  }

  loadFromMatchHeader(header: schema.MatchHeader) {
    const map = header.map();
    const bodies = map.bodies(this._bodiesSlot);
    if (bodies) {
      this.insertBodies(bodies);
    }
    const trees = map.trees();
    if (trees) {
      this.insertTrees(map.trees());
    }
    const minCorner = map.minCorner();
    this.minCorner.x = minCorner.x();
    this.minCorner.y = minCorner.y();
    const maxCorner = map.maxCorner();
    this.maxCorner.x = maxCorner.x();
    this.maxCorner.y = maxCorner.y();
    const name = map.name() as string;
    if (name) {
      this.mapName = map.name() as string;
    }
  }

  /**
   * Create a copy of the world in its current state.
   */
  copy(): GameWorld {
    const result = new GameWorld(this.meta);
    result.copyFrom(this);
    return result;
  }

  copyFrom(source: GameWorld) {
    this.turn = source.turn;
    this.minCorner = source.minCorner;
    this.maxCorner = source.maxCorner;
    this.mapName = source.mapName;
    this.bodies.copyFrom(source.bodies);
    this.bullets.copyFrom(source.bullets);
  }

  /**
   * Process a set of changes.
   */
  processDelta(delta: schema.Round) {
    if (delta.roundID() != this.turn + 1) {
      throw new Error(`Bad Round: this.turn = ${this.turn}, round.roundID() = ${delta.roundID()}`);
    }

    // Increase the turn count
    this.turn += 1;

    // Simulate deaths
    if (delta.diedIDsLength() > 0) {
      this.bodies.deleteBulk(delta.diedIDsArray());
    }
    if (delta.diedBulletIDsLength() > 0) {
      this.bullets.deleteBulk(delta.diedBulletIDsArray());
    }

    // Simulate changed health levels
    if (delta.healthChangedIDsLength() > 0) {
      this.bodies.alterBulk({
        id: delta.healthChangedIDsArray(),
        health: delta.healthChangeLevelsArray()
      });
    }

    // Simulate movement
    const movedLocs = delta.movedLocs(this._vecTableSlot);
    if (movedLocs) {
      this.bodies.alterBulk({
        id: delta.movedIDsArray(),
        x: movedLocs.xsArray(),
        y: movedLocs.ysArray(),
      });
    }

    // Simulate spawning
    const bodies = delta.spawnedBodies(this._bodiesSlot);
    if (bodies) {
      this.insertBodies(bodies);
    }

    // Simulate spawning
    const bullets = delta.spawnedBullets(this._bulletsSlot);
    if (bullets) {
      this.insertBullets(bullets);
    }
  }

  private insertBodies(bodies: schema.SpawnedBodyTable) {
    const locs = bodies.locs(this._vecTableSlot);
    // Note: this allocates 6 objects with each call.
    // (One for the container, one for each TypedArray.)
    // All of the objects are small; the TypedArrays are basically
    // (pointer, length) pairs.
    // You can't reuse TypedArrays easily, so I'm inclined to
    // let this slide for now.
    const startIndex = this.bodies.insertBulk({
      id: bodies.robotIDsArray(),
      team: bodies.teamIDsArray(),
      type: bodies.typesArray(),
      x: locs.xsArray(),
      y: locs.ysArray(),
    });

    // Extra initialization
    const endIndex = startIndex + bodies.robotIDsLength();
    const typeArray = this.bodies.arrays['type'];
    const radiusArray = this.bodies.arrays['radius'];
    const healthArray = this.bodies.arrays['health'];
    for (let i = startIndex; i < endIndex; i++) {
      const type = typeArray[i];
      const typeInfo = this.meta.types[type];
      radiusArray[i] = typeInfo.radius;
      healthArray[i] = typeInfo.startHealth;
    }
  }

  private insertBullets(bullets: schema.SpawnedBulletTable) {
    const locs = bullets.locs(this._vecTableSlot);
    const vels = bullets.vels(this._vecTableSlot);

    const startI = this.bullets.insertBulk({
      id: bullets.robotIDsArray(),
      x: locs.xsArray(),
      y: locs.ysArray(),
      velX: vels.xsArray(),
      velY: vels.ysArray(),
      damage: bullets.damagesArray(),
    });

    // There may be an off-by-one error here but I think this is right
    StructOfArrays.fill(this.bullets.arrays['spawnedTime'], this.turn, startI, this.bullets.length);
  }

  private insertTrees(trees: schema.NeutralTreeTable) {
    const locs = trees.locs(this._vecTableSlot);

    const startI = this.bodies.insertBulk({
      id: trees.robotIDsArray(),
      radius: trees.radiiArray(),
      health: trees.healthsArray(),
      x: locs.xsArray(),
      y: locs.ysArray(),
    });

    StructOfArrays.fill(
      this.bodies.arrays['team'],
      NEUTRAL_TEAM,
      startI,
      this.bodies.length
    );

    StructOfArrays.fill(
      this.bodies.arrays['type'],
      schema.BodyType.TREE_NEUTRAL,
      startI,
      this.bodies.length
    );
  }
}

// TODO(jhgilles): encode in flatbuffers
const NEUTRAL_TEAM = 2;
