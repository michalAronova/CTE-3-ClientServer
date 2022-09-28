package engine.decipherManager.dictionary;

import schema.generated.CTEDictionary;

import java.util.*;
import java.util.stream.Collectors;

public class Dictionary {
    private Set<String> words;
    private List<String> excluded;


    public Dictionary(CTEDictionary cteDictionary){
        String cteWords = cteDictionary.getWords();
        String cteExcluded = cteDictionary.getExcludeChars();

        excluded = new ArrayList<>();
        for (Character c: cteExcluded.toCharArray()) {
            excluded.add(c.toString());
        }

        cteWords = removeExcludedChars(cteWords);
        this.words = new HashSet<>(Arrays.asList(cteWords.split(" ")));
        this.words = this.words.stream().map(String::toUpperCase).collect(Collectors.toSet());
    }

    public Set<String> getWords() {
        return words;
    }

    public List<String> getExcluded() {
        return excluded;
    }

    public Dictionary(){
        excluded = new ArrayList<>();
        excluded.add("!");
        excluded.add(",");
        String newWords = "hello,,,!, world!!!!";
        String another = "tes,,ting!!";
        List<String> list = new ArrayList<>();
        list.add(newWords);
        list.add(another);

        list = removeExcludedChars(list);
        list.forEach(System.out::println);
    }

    public static void main(String[] args) {
        List<String> test = new ArrayList<>();
        test.add("michal");
        test.add("michal");

        Set<String> set = new HashSet<>(test);
        System.out.println(set);
    }

    public String removeExcludedChars(String line){
        String cleaned = line;
        for (String s: excluded) {
            cleaned = cleaned.replace(s, "");
        }
        return cleaned;
    }

    public List<String> removeExcludedChars(List<String> lines){
        List<String> cleaned = new ArrayList<>();
        for (String line : lines) {
            cleaned.add(removeExcludedChars(line));
        }
        return cleaned;
    }

    public boolean isInDictionary(String word) {
        return words.contains(word.toUpperCase());
    }

    public boolean areAllWordsInDictionary(String words){
        words = removeExcludedChars(words);
        List<String> wordsList = new LinkedList<>(Arrays.asList(words.split(" ")));
        for(String word: wordsList){
            if(!isInDictionary(word)){
                return false;
            }
        }
        return true;
    }
}
