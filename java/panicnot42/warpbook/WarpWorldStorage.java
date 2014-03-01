package panicnot42.warpbook;

import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class WarpWorldStorage extends WorldSavedData
{
  public class Waypoint
  {
    public String name;
    public int x, y, z, dim;

    public Waypoint(String name, int x, int y, int z, int dim)
    {
      this.name = name;
      this.x = x;
      this.y = y;
      this.z = z;
      this.dim = dim;
    }
  }

  private HashMap<String, Waypoint> waypoints;

  private final static String IDENTIFIER = "WarpBook";

  public WarpWorldStorage(String identifier)
  {
    super(identifier);
    waypoints = new HashMap<String, Waypoint>();
  }

  public static WarpWorldStorage instance(World world)
  {
    if (world.mapStorage.loadData(WarpWorldStorage.class, IDENTIFIER) == null) world.mapStorage.setData(IDENTIFIER, new WarpWorldStorage(IDENTIFIER));
    return (WarpWorldStorage)world.mapStorage.loadData(WarpWorldStorage.class, IDENTIFIER);
  }

  @Override
  public void readFromNBT(NBTTagCompound var1)
  {
    NBTTagList tags = var1.getTagList("waypoints", new NBTTagCompound().getId());
    for (int i = 0; i < tags.tagCount(); ++i)
      waypoints.put(tags.getCompoundTagAt(i).getString("name"), new Waypoint(tags.getCompoundTagAt(i).getString("friendlyName"), tags.getCompoundTagAt(i).getInteger("x"), tags.getCompoundTagAt(i)
          .getInteger("y"), tags.getCompoundTagAt(i).getInteger("z"), tags.getCompoundTagAt(i).getInteger("dim")));
  }

  @Override
  public void writeToNBT(NBTTagCompound var1)
  {
    NBTTagList tags = new NBTTagList();
    for (Entry<String, Waypoint> wps : waypoints.entrySet())
    {
      NBTTagCompound wp = new NBTTagCompound();
      wp.setString("friendlyName", wps.getValue().name);
      wp.setInteger("x", wps.getValue().x);
      wp.setInteger("y", wps.getValue().y);
      wp.setInteger("z", wps.getValue().z);
      wp.setInteger("dim", wps.getValue().dim);
      wp.setString("name", wps.getKey());
      tags.appendTag(wp);
    }
    var1.setTag("waypoints", tags);
  }

  public boolean waypointExists(String name)
  {
    return waypoints.keySet().contains(name);
  }

  public Waypoint getWaypoint(String name)
  {
    return waypoints.get(name);
  }

  public void addWaypoint(String name, Waypoint point)
  {
    waypoints.put(name, point);
    this.markDirty();
  }

  public String[] listWaypoints()
  {
    String[] wplist = new String[waypoints.size()];
    return (String[])waypoints.keySet().toArray(wplist);
  }

  public boolean deleteWaypoint(String waypoint)
  {
    this.markDirty();
    return waypoints.remove(waypoint) != null;
  }
}