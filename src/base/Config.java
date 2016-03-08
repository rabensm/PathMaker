package base;

/**
 * Some global config values
 */
public class Config {
    // depth of each pass when cutting a path
    static final public double PassDepth = 0.6;

    // g-code feed rate
    static final public double FeedRate = 250;

    // the height to move above the work surface for rapid positioning (G00)
    static final public double SafeHeight = 1.5;

    // the height above the surface to start feed cut
    static final public double StartHeight = 0.2;
}
