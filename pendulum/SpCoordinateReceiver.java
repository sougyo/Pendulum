package pendulum;
import javax.vecmath.Color3f;

public interface SpCoordinateReceiver {

	public abstract void setCoordinate(SpCoordinate coordinate);

	public abstract void setCoordinate(SpCoordinate coordinate, Color3f color);

	public abstract void clear();
}