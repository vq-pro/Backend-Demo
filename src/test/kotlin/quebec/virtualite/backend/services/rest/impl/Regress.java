package quebec.virtualite.backend.services.rest.impl;

public class Regress
{
    public static void main(String[] args)
    {
        for (int soe = 100; soe >= 0; soe -= 5)
        {
            System.out.println(soe + " " + regress(soe));
        }
    }

    private static double regress(double x)
    {
        double[] terms = {
            -8.1898886754916411e-002,
            8.6072498260937214e-002,
            5.1953408611314505e-003
        };

        double t = 1;
        double r = 0;
        for (double c : terms)
        {
            r += c * t;
            t *= x;
        }
        return r;
    }
}
