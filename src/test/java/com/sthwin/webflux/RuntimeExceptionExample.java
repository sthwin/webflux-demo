package com.sthwin.webflux;

/**
 * Created by User
 * Date: 2020. 6. 19. 오전 8:41
 */
public class RuntimeExceptionExample {
    public static void main(String[] args) {
        try {
            getCal();
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static int getCal() {
        for (int i = 0; i < 5; i++) {
            if (i == 1)
                throw new TestRuntimeException("getCal");
        }
        return 0;
    }
}
