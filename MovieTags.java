import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MovieTags {

    //class TagEntries to make an array of objects (String and int)
    public static class TagEntries implements Comparable<TagEntries> {
        public String tags;
        public int count;
        public TagEntries(String movietag, int moviecount){
            this.tags = movietag;
            this.count = moviecount;
        }

        public void incCount(){
            count++;
        }

        public int compareTo(TagEntries other) {
            return this.count - other.count;
        }

        public String getTag()
        {
            return this.tags;
        }

        public int getCount()
        {
            return this.count;
        }

        public String output(){
            return count + ": "+ tags;
        }

    }

    private static final String COMMA_DELIMITER = ",";


    /**
     * Method to Merge sort all elements with a nested for loop
     * @param arr Arraylist of all movie tags
     * @return sorted arraylist
     */


    List<TagEntries> DescSorting(List<TagEntries> arr, int start, int end) {
        if (start < end) {
            int mid = (start + end) / 2;
            DescSorting(arr, start, mid);
            DescSorting(arr, mid + 1, end);
            merge(arr, start, mid, end);
        }
        return arr;
    }

    public static void merge(List<TagEntries> arr, int start, int mid, int end) {
        ArrayList<Object> temp = new ArrayList<>();
        int i = start;
        int j = mid + 1;
        while (i <= mid && j <= end) {
            if (arr.get(i).compareTo(arr.get(j)) <= 0) {
                temp.add(arr.get(i));
                i++;
            } else {
                temp.add(arr.get(j));
                j++;
            }
        }
        while (i <= mid) {
            temp.add(arr.get(i));
            i++;
        }
        while (j <= end) {
            temp.add(arr.get(j));
            j++;
        }
        for (i = start; i <= end; i++) {
            arr.set(i, (TagEntries) temp.get(i - start));
        }
    }

    /**
     * Method uses compareTo function to list least popular tags
     * @param arr Arraylist of all movie tags
     * @return sorted arraylist
     */
    List<TagEntries> abcSorting(List<TagEntries> arr) {

        String temp;
        for (int i = 0; i < arr.size(); i++) {
            for (int j = i + 1; j < arr.size(); j++) {
                // to compare one string with other strings
                if (arr.get(i).tags.trim().compareTo(arr.get(j).tags) > 0) {
                    // swapping
                    temp = arr.get(i).tags;
                    arr.get(i).tags = arr.get(j).tags;
                    arr.get(j).tags = temp;
                }
            }
        }
        return arr;
    }



    /**
     * Method prints 3 most popular movie tags
     * @param arr Arraylist of all movie tags
     */
    void MostPopular(List<TagEntries> arr){
        System.out.println("==========================================");
        System.out.println("*** Highest 3 movies by count ***");
        System.out.println(arr.get(arr.size()-1).output());
        System.out.println(arr.get(arr.size()-2).output());
        System.out.println(arr.get(arr.size()-3).output());
    }



    /**
     * Method prints 3 least popular tags
     * @param arr Arraylist of all movie tags
     */
    void LeastPopular(List<TagEntries> arr){
        List<TagEntries> temp = new ArrayList<>();
        for(int i = 0; i < arr.size(); i++){
            if(arr.get(i).count == 1){
                temp.add(arr.get(i));
            }
        }

        List<TagEntries> curr = abcSorting(temp);
        System.out.println("*** Lowest 3 movies by count ***");
        System.out.println(curr.get(0).output());
        System.out.println(curr.get(1).output());
        System.out.println(curr.get(2).output());
        System.out.println("==========================================");

    }



    /**
     * Method searches for tags based on the input count
     * @param arr Arraylist of all movie tags
     */
    void FindTags(List<TagEntries> arr){
        Scanner input = new Scanner(System.in);
        int choice;
        int cnt=0;

        System.out.print("Count to search for: ");

        try
        {
            choice = input.nextInt();
            System.out.println("Tags with " + choice + " occurrences:");
            for(int i = 0; i < arr.size(); i++){
                if(arr.get(i).count == choice){
                    System.out.println(arr.get(i).tags);
                    cnt++;
                }
            }

            if(cnt == 0)
                System.out.println("There are no tags");
        }
        catch (NumberFormatException | InputMismatchException e)
        {
            System.out.println("Your input is invalid! It should be a number");
        }
    }


    /**
     * Method prints number of occurrences of a particular input movie tag
     * @param arr Arraylist of all movie tags
     */
    void FindCount(List<TagEntries> arr){
        Scanner input = new Scanner(System.in);
        String choice;
        int cnt=0;

        System.out.print("Tag to search for: ");
        choice = input.nextLine();
        for(int i = 0; i < arr.size(); i++){
            if(arr.get(i).tags.equals(choice)){
                System.out.println("Tag \"" + choice +"\" occurred " + arr.get(i).count + " times");
                cnt++;
            }
        }

        if(cnt == 0)
            System.out.println("Tag \"" + choice +"\" does not exist.");
    }

    int search(List<TagEntries> arr, String x)
    {
        for (int ind = 0; ind < arr.size(); ind++) {
            // Return the index of the element if the element is found
            if (arr.get(ind).getTag().equals(x))
                return ind;
        }
        return -1; // return -1 if the element is not found
    }



    /**
     * Main method to create arraylist of tag entries and calls functions to sort.
     * @param args args[0] is the path of the tags.csv file
     * @throws FileNotFoundException If the file does not exist.
     */

    public static void main(String[] args) throws FileNotFoundException {
        MovieTags mt = new MovieTags();
        String path=args[0];

        //Part 1: reading the file
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Reading data file... (may take 5 minutes)");

        // Create a list of only the movie tags
        List<String> movieTags = new ArrayList<>();

        for (List<String> record : records) {
            movieTags.add(record.get(2));
        }

        List<TagEntries> tagEntries = new ArrayList<>();

        for (String movieTag : movieTags) {
            int check = mt.search(tagEntries, movieTag);
            if (check == -1) {
                tagEntries.add(new TagEntries(movieTag, 1));
            } else {
                tagEntries.get(check).incCount();
            }
        }
        //Part 2: Finding the 3 most and 3 least popular tags
        mt.MostPopular(mt.DescSorting(tagEntries, 0, tagEntries.size()-1));
        mt.LeastPopular(tagEntries);

        //Part 3: Finding tags by count or tag count by tag name
        Scanner input = new Scanner(System.in);
        String choice;
        while (true) {
            System.out.print("Search by Tag or Tag Count? (Enter T or C... or EXIT to exit): ");
            choice = input.next();
            if (choice.equalsIgnoreCase("T")){
                mt.FindCount(tagEntries);
            }
            else if(choice.equalsIgnoreCase("C")){
                mt.FindTags(tagEntries);
            }
            else if(choice.equalsIgnoreCase("exit")){
                System.out.println("Bye!");
                break;
            }
            else{
                System.out.println("ERROR Please enter a valid choice (T or C... or EXIT to exit)");
            }
        }
    }
}

