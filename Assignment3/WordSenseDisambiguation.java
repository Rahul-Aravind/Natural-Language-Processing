import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class WordSenseDisambiguation {
	private static String sentence = "The bank can guarantee deposits will eventually cover future tuition costs "
			+ "because it invests in adjustable-rate mortgage securities";
	private static String word = "bank";
	private static Vector<String>senseGlossExampleSet;
	private static Vector<String>prunedSenseGlossExampleSet;
	private static WordNetDatabase wordNetDatabase;
	private static Vector<String> stopWords;
	
	public static void initialize() {
		System.setProperty("wordnet.database.dir", "C:\\Program Files (x86)\\WordNet\\2.1\\dict");
		wordNetDatabase = WordNetDatabase.getFileInstance();
		senseGlossExampleSet = new Vector<String>();
		stopWords = new Vector<String>();
		prunedSenseGlossExampleSet = new Vector<String>();
	}
	
	public static void loadStopWords() {
		String stopWord = "a_about_above_across_after_again_against_all_almost_alone_along_already_also_although_always_among_an_and_another_any_anybody_anyone_anything_anywhere_are_area_areas_around_as_ask_asked_asking_asks_at_away_b_back_backed_backing_backs_be_became_because_become_becomes_been_before_began_behind_being_beings_best_better_between_big_both_but_by_c_came_can_cannot_case_cases_certain_certainly_clear_clearly_come_could_d_did_differ_different_differently_do_does_done_down_down_downed_downing_downs_during_e_each_early_either_end_ended_ending_ends_enough_even_evenly_ever_every_everybody_everyone_everything_everywhere_f_face_faces_fact_facts_far_felt_few_find_finds_first_for_four_from_full_fully_further_furthered_furthering_furthers_g_gave_general_generally_get_gets_give_given_gives_go_going_good_goods_got_great_greater_greatest_group_grouped_grouping_groups_h_had_has_have_having_he_her_here_herself_high_high_high_higher_highest_him_himself_his_how_however_i_if_important_in_interest_interested_interesting_interests_into_is_it_its_itself_j_just_k_keep_keeps_kind_knew_know_known_knows_l_large_largely_last_later_latest_least_less_let_lets_like_likely_long_longer_longest_m_made_make_making_man_many_may_me_member_members_men_might_more_most_mostly_mr_mrs_much_must_my_myself_n_necessary_need_needed_needing_needs_never_new_new_newer_newest_next_no_nobody_non_noone_not_nothing_now_nowhere_number_numbers_o_of_off_often_old_older_oldest_on_once_one_only_open_opened_opening_opens_or_order_ordered_ordering_orders_other_others_our_out_over_p_part_parted_parting_parts_per_perhaps_place_places_point_pointed_pointing_points_possible_present_presented_presenting_presents_problem_problems_put_puts_q_quite_r_rather_really_right_right_room_rooms_s_said_same_saw_say_says_second_seconds_see_seem_seemed_seeming_seems_sees_several_shall_she_should_show_showed_showing_shows_side_sides_since_small_smaller_smallest_so_some_somebody_someone_something_somewhere_state_states_still_still_such_sure_t_take_taken_than_that_the_their_them_then_there_therefore_these_they_thing_things_think_thinks_this_those_though_thought_thoughts_three_through_thus_to_today_together_too_took_toward_turn_turned_turning_turns_two_u_under_until_up_upon_us_use_used_uses_v_very_w_want_wanted_wanting_wants_was_way_ways_we_well_wells_went_were_what_when_where_whether_which_while_who_whole_whose_why_will_with_within_without_work_worked_working_works_would_x_y_year_years_yet_you_young_younger_youngest_your_yours_z";
		String[] set = stopWord.split("_");
		for(String s : set) {
			stopWords.add(s);
		}
		//System.out.println(stopWords);
	}
	
	public static void pruneSpecialCharacters() {
		for(String s : senseGlossExampleSet) {
			String prunedStr = "";
			int len = s.length();
			for(int i = 0; i < len; i++) {
				if((s.charAt(i) >= 'a' && s.charAt(i) <= 'z') ||  
						(s.charAt(i) >= 'A' && s.charAt(i) <= 'Z') || 
						(s.charAt(i) == '_') || (s.charAt(i) == ' ')) {
					prunedStr += s.charAt(i);
				}
			}
			prunedSenseGlossExampleSet.add(prunedStr);
		}
		
	}
	
	public static void computeOverlap() {
		String bestSense = "";
		int maxOverlap = 0;
		
		Map<String, Boolean> sentenceHashMap = new HashMap<String, Boolean>();
		
		//prune the sentence
		String prunedSentence = "";
		for(int i = 0; i < sentence.length(); i++) {
			if((sentence.charAt(i) >= 'a' && sentence.charAt(i) <= 'z') ||  
					(sentence.charAt(i) >= 'A' && sentence.charAt(i) <= 'Z') || 
					(sentence.charAt(i) == ' ') || (sentence.charAt(i) == '-')) {
				if(sentence.charAt(i) == '-')
					prunedSentence += ' ';
				else
					prunedSentence += sentence.charAt(i);
			}
		}
		
		//System.out.println(prunedSentence);
		
		String[] sentenceSplit = prunedSentence.split(" ");
		
		for(String s : sentenceSplit) {
			if(s.equalsIgnoreCase(word) == false && stopWords.contains(s) == false) {
				sentenceHashMap.put(s, true);
			}
		}
		
		for(String s : prunedSenseGlossExampleSet) {
			int overlap = 0;
			String[] senseAndGlossEx = s.split("_");
			String senseDefinition = "";
			String Examples = "";
			if(senseAndGlossEx.length == 3) {
				Examples = senseAndGlossEx[2];
			}
			
			senseDefinition = senseAndGlossEx[1];
			String[] glossExamplesSplit = (senseDefinition + " " + Examples).split(" ");
			System.out.println("****************************************");
			System.out.println("sense: " + senseDefinition);
			System.out.println("Overlapping words: ");
			for(String str : glossExamplesSplit) {
				if(sentenceHashMap.containsKey(str))  {
					overlap++;
					System.out.print(str + " ");
				}
				
				if(overlap > maxOverlap) {
					maxOverlap = overlap;
					bestSense = senseDefinition;
				}
			}
			System.out.println("\n" + "Overlap :" + overlap + "\n" + "******************************************");
		}
		
		System.out.println("****************************************");
		System.out.println("Best Suited Sense with respect to the context in the sentence is: " + bestSense);
		System.out.println("Max Overlap " + maxOverlap);
		System.out.println("****************************************");
	}
	
	public static void main(String[] args) {
		
		initialize();
		Synset[] synsetList = wordNetDatabase.getSynsets(word);
		loadStopWords();
		
		for(Synset synset : synsetList) {
			String wordSense = (synset.getWordForms())[0];
			String gloss = synset.getDefinition();
			String[] examples = synset.getUsageExamples();
			String Example = "";
			for(String example : examples) {
				Example += example + " ";
			}
			String senseGlossExample = wordSense + "_" + gloss + "_" + Example;
			senseGlossExampleSet.add(senseGlossExample);
		}
		
		pruneSpecialCharacters();
		computeOverlap();
	}

}
