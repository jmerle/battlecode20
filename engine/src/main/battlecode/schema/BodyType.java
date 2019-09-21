// automatically generated by the FlatBuffers compiler, do not modify

package battlecode.schema;

/**
 * The possible types of things that can exist.
 * Note that neutral trees and bullets are not treated as bodies.
 */
public final class BodyType {
  private BodyType() { }
  /**
   * Archons are the mobile equivalent of a HQ whose sole purpose is to hire
   * gardeners to maintain the land.
   */
  public static final byte ARCHON = 0;
  /**
   * Gardeners are caretakers of the land, planting and watering Bullet Trees
   * while also cultivating all other player robots.
   */
  public static final byte GARDENER = 1;
  /**
   * Lumberjacks are melee units equipped for felling trees.
   */
  public static final byte LUMBERJACK = 2;
  /**
   * Soldiers are all-around units with a tricky shot.
   */
  public static final byte SOLDIER = 3;
  /**
   * Tanks are large, slow units with powerful bullets.
   */
  public static final byte TANK = 4;
  /**
   * Scouts are fast units that move around without obstruction.
   */
  public static final byte SCOUT = 5;
  /**
   * A tree that belongs to a team and produces bullets.
   */
  public static final byte TREE_BULLET = 6;
  /**
   * A neutral tree.
   * This is included for convenience; note this value SHALL NOT appear in
   * a SpawnedBodyTable.
   */
  public static final byte TREE_NEUTRAL = 7;
  /**
   * A bullet that moves in a perfectly straight line.
   * Note: bullet location updates are not sent; a bullet is defined to be
   * in position loc + dt * vel after dt seconds.
   * This allows us some significant space savings, since there are lots
   * of bullets, and we don't need to send position updates.
   * The event stream will say if a bullet has been destroyed.
   * This is included for convenience; note this value SHALL NOT appear in
   * a SpawnedBodyTable.
   */
  public static final byte BULLET = 8;
  /**
   * Indicates that there is no body.
   * May only appear in the containedBodies field of NeutralTreeTable.
   */
  public static final byte NONE = 9;

  public static final String[] names = { "ARCHON", "GARDENER", "LUMBERJACK", "SOLDIER", "TANK", "SCOUT", "TREE_BULLET", "TREE_NEUTRAL", "BULLET", "NONE", };

  public static String name(int e) { return names[e]; }
}

