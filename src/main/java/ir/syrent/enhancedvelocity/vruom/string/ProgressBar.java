package ir.syrent.enhancedvelocity.vruom.string;

public class ProgressBar {

    public static String progressBar(int current, int max, int total, String completeString, String notCompleteString) {
        float percent = (float) current / max;
        int progressBars = (int) (total * percent);

        return repeat(completeString, progressBars) + repeat(notCompleteString, total - progressBars);
    }

    private static String repeat(String string, int number) {
        return new String(new char[Math.max(0, number)]).replace("\0", string);
    }

}
