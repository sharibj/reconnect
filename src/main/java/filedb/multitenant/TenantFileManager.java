package filedb.multitenant;

import spring.security.TenantContext;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TenantFileManager {
    private static final String BASE_DATA_DIR = "data";
    
    public static String getTenantDataPath(String fileName) {
        String tenant = TenantContext.getCurrentTenant();
        if (tenant == null) {
            throw new IllegalStateException("No tenant context available");
        }
        
        // Create tenant-specific directory path
        Path tenantDir = Paths.get(BASE_DATA_DIR, "tenants", tenant);
        File dir = tenantDir.toFile();
        
        // Create directory if it doesn't exist
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        return tenantDir.resolve(fileName).toString();
    }
    
    public static String getTenantDirectory() {
        String tenant = TenantContext.getCurrentTenant();
        if (tenant == null) {
            throw new IllegalStateException("No tenant context available");
        }
        
        Path tenantDir = Paths.get(BASE_DATA_DIR, "tenants", tenant);
        File dir = tenantDir.toFile();
        
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        return tenantDir.toString();
    }
}
