package br.edu.ufabc.energy.monitoring;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Energy monitoring utility class using jRAPL library for Intel processors on Linux.
 * Requires the jRAPL-3.0.jar library and appropriate system permissions.
 * 
 * This class provides static methods for easy integration with benchmark code.
 */
public class EnergyMonitor {
    
    private static final Logger logger = Logger.getLogger(EnergyMonitor.class.getName());
    
    private static boolean initialized = false;
    private static boolean measurementActive = false;
    private static double startEnergy = 0.0;
    private static boolean jraplAvailable = false;
    
    // Reflection-based access to jRAPL classes to avoid compile-time dependency
    private static Class<?> energyCheckUtilsClass;
    private static Method initMethod;
    private static Method profileInitMethod;
    private static Method energyStatsMethod;
    private static Method getCPUEnergyMethod;
    private static Method getDRAMEnergyMethod;
    
    /**
     * Initialize the energy monitoring system.
     * Attempts to load and initialize jRAPL library.
     */
    public static synchronized void initialize() {
        if (initialized) {
            return;
        }
        
        try {
            // Try to load jRAPL classes using reflection
            energyCheckUtilsClass = Class.forName("jrapl.EnergyCheckUtils");
            
            // Get required methods
            initMethod = energyCheckUtilsClass.getMethod("init");
            profileInitMethod = energyCheckUtilsClass.getMethod("ProfileInit");
            energyStatsMethod = energyCheckUtilsClass.getMethod("energyStatCheck");
            getCPUEnergyMethod = energyCheckUtilsClass.getMethod("getCPUEnergy");
            getDRAMEnergyMethod = energyCheckUtilsClass.getMethod("getDRAMEnergy");
            
            // Initialize jRAPL
            initMethod.invoke(null);
            profileInitMethod.invoke(null);
            
            jraplAvailable = true;
            logger.info("jRAPL energy monitor initialized successfully");
            
        } catch (ClassNotFoundException e) {
            logger.warning("jRAPL library not found in classpath. Please add jRAPL-3.0.jar to classpath.");
            jraplAvailable = false;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to initialize jRAPL energy monitor", e);
            jraplAvailable = false;
        }
        
        initialized = true;
    }
    
    /**
     * Start a new energy measurement session.
     * Call this method before executing the code to be measured.
     */
    public static void startMeasurement() {
        if (!initialized) {
            initialize();
        }
        
        if (!jraplAvailable) {
            measurementActive = true;
            return;
        }
        
        try {
            // Update energy statistics and get baseline reading
            energyStatsMethod.invoke(null);
            startEnergy = getCurrentTotalEnergy();
            measurementActive = true;
            
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to start energy measurement", e);
            measurementActive = false;
        }
    }
    
    /**
     * Stop the current energy measurement session and return the measured energy.
     * 
     * @return the energy consumed during the measurement session in Joules, or 0 if jRAPL is not available
     */
    public static double stopMeasurement() {
        if (!initialized || !measurementActive) {
            logger.warning("No active measurement session");
            return 0.0;
        }
        
        measurementActive = false;
        
        if (!jraplAvailable) {
            return 0.0;
        }
        
        try {
            // Update energy statistics and get final reading
            energyStatsMethod.invoke(null);
            double endEnergy = getCurrentTotalEnergy();
            
            double consumedEnergy = endEnergy - startEnergy;
            return Math.max(0.0, consumedEnergy); // Ensure non-negative result
            
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to stop energy measurement", e);
            return 0.0;
        }
    }
    
    /**
     * Get the current total energy reading (CPU + DRAM)
     */
    private static double getCurrentTotalEnergy() throws Exception {
        double cpuEnergy = (Double) getCPUEnergyMethod.invoke(null);
        double dramEnergy = (Double) getDRAMEnergyMethod.invoke(null);
        return cpuEnergy + dramEnergy;
    }
    
    /**
     * Get the unit of measurement for energy values returned by stopMeasurement().
     * 
     * @return "Joules" if jRAPL is available, "N/A" otherwise
     */
    public static String getMeasurementUnit() {
        return jraplAvailable ? "Joules" : "N/A";
    }
    
    /**
     * Check if jRAPL library is available and functional.
     * 
     * @return true if jRAPL is available, false otherwise
     */
    public static boolean isJRAPLAvailable() {
        if (!initialized) {
            initialize();
        }
        return jraplAvailable;
    }
    
    /**
     * Get information about the energy monitoring system.
     * 
     * @return description of the monitoring implementation
     */
    public static String getImplementationInfo() {
        if (jraplAvailable) {
            return "jRAPL (Java Runtime for Application Power and energy Library) - Hardware-based energy measurement using Intel RAPL";
        } else {
            return "jRAPL not available - ensure jRAPL-3.0.jar is in classpath and running on supported Linux system";
        }
    }
    
    /**
     * Check if energy monitoring is available on this system.
     * 
     * @return true if jRAPL is available, false otherwise
     */
    public static boolean isAvailable() {
        return isJRAPLAvailable();
    }
}