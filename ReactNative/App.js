/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Fragment, Component} from 'react';
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  ViewProps,
  Text,
  StatusBar,
  Dimensions,
  TouchableHighlight,
  Colors,
  Image,
} from 'react-native';

import TwoPaneView from './TwoPaneView'

const App = () => {
  return (
    <TwoPaneView panePriority='paneA' panePriorityVerticalSpanning='paneB' style={{backgroundColor:'green'}}>
      <View style={styles.sectionContainer}>
        <TouchableHighlight>
          <View>
            <Text style={styles.sectionTitle}>Naches Peak Loop</Text>
            <Text style={styles.sectionDescription}> Mount Rainier area</Text>
          </View>
        </TouchableHighlight>
      </View>
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={styles.scrollView}
        >
        <View style={styles.sectionContainer}>            
          <View>
            <Text style={styles.subSectionTitle}>Location</Text>
            <Text style={styles.subSectionDescription}>
            Mount Rainier Area -- SE - Cayuse Pass/Stevens Canyon
            </Text>
            <Text style={styles.subSectionTitle}>Length</Text>
            <Text style={styles.subSectionDescription}>
            3.2 miles, roundtrip
            </Text>
            <Text style={styles.subSectionTitle}>Elevation</Text>
            <Text style={styles.subSectionDescription}>
            Gain: 600 ft. - Highest Point: 5850 ft.
            </Text>
          </View>
          <View>
            <Image style={{width: 300, height:200}}
              source={{uri: 'https://www.wta.org/site_images/hikes/jim-mt-rainier-wta.jpg/@@images/edb871c7-6246-43a3-9b5e-25e06e04bd9f.jpeg'}}/>
          </View>
          <View>
            <Text style={styles.bodyDescription}>
            This popular hike provides a range of alpine experiences in a short loop. Walk along a hillside above a small valley, pause at a viewpoint overlooking a lake, and stroll through grassy meadows with stunning views of Mount Rainier. In season enjoy an abundance of wildflowers or perhaps a handful of huckleberries.
            </Text>
            <Text style={styles.bodyDescription}>
            The hike follows the Pacific Crest Trail on the northern flank of Naches Peak and the Naches Peak Trail on the southern flank. The loop is best done clockwise for outstanding views of Mount Rainier.
            </Text>
            <Text style={styles.bodyDescription}>
            This hike begins at Tipsoo Lake and then traverses a steep hillside on the northern flank of Naches Peak. As you climb a gentle slope, you will look down on a small lush valley that is a wildflower heaven. In late July and early August you may be treated to a flower festival including blue lupine, white bistort, and magenta paintbrush. Along the way you will pass an unnamed lakelet where you may be tempted to take off your boots and go wading.
            </Text>
            <Text style={styles.bodyDescription}>
            As the loop wraps around the east side of Naches Peak, the trail comes to a viewpoint with Dewey Lake over 600 feet below. The smooth rock bench here makes an excellent place to eat lunch or take a break. This is a popular place, however, so be prepared to share the experience with others.
            </Text>
            <Text style={styles.bodyDescription}>
            Just after the Dewey Lake viewpoint, the Pacific Crest Trail comes to a junction with the Naches Peak Loop trail. Go right and follow the trail along the southern side of Naches Peak,where you will cross alpine meadows with stunning views of Mount Rainier. This is an exceptionally picturesque area, and it is easy to get fantastic photos here. Many people feel this is the highlight of the loop. Follow this trail back to the Tipsoo Lake parking lot to complete your loop.
            </Text>
            <Text style={styles.bodyDescription}>
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec ut lorem commodo, consequat erat a, congue libero. Sed eget elementum sem. Nullam sit amet venenatis massa. Quisque efficitur erat eu urna porttitor sollicitudin. Mauris eu pharetra mauris. Aliquam ut imperdiet dui. Cras molestie dignissim est, id congue nisi dictum a. Cras rutrum erat nec nulla commodo, efficitur rutrum orci aliquet. Suspendisse pellentesque, dui id aliquet suscipit, arcu velit sollicitudin enim, quis consequat tortor libero a turpis. Etiam luctus ligula est, non porta dolor tincidunt a. Etiam quis enim efficitur, finibus odio et, dictum nulla. Nullam fringilla massa at auctor ultrices. Phasellus ac mi consequat, aliquam lacus non, placerat neque. Suspendisse a rhoncus elit. Quisque ultricies libero et ipsum condimentum, sit amet euismod purus condimentum. Nullam euismod elit sed justo vulputate, et laoreet justo pharetra.              
            </Text>
            <Text style={styles.bodyDescription}>
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec ut lorem commodo, consequat erat a, congue libero. Sed eget elementum sem. Nullam sit amet venenatis massa. Quisque efficitur erat eu urna porttitor sollicitudin. Mauris eu pharetra mauris. Aliquam ut imperdiet dui. Cras molestie dignissim est, id congue nisi dictum a. Cras rutrum erat nec nulla commodo, efficitur rutrum orci aliquet. Suspendisse pellentesque, dui id aliquet suscipit, arcu velit sollicitudin enim, quis consequat tortor libero a turpis. Etiam luctus ligula est, non porta dolor tincidunt a. Etiam quis enim efficitur, finibus odio et, dictum nulla. Nullam fringilla massa at auctor ultrices. Phasellus ac mi consequat, aliquam lacus non, placerat neque. Suspendisse a rhoncus elit. Quisque ultricies libero et ipsum condimentum, sit amet euismod purus condimentum. Nullam euismod elit sed justo vulputate, et laoreet justo pharetra.              
            </Text>
            <Text style={styles.bodyDescription}>
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec ut lorem commodo, consequat erat a, congue libero. Sed eget elementum sem. Nullam sit amet venenatis massa. Quisque efficitur erat eu urna porttitor sollicitudin. Mauris eu pharetra mauris. Aliquam ut imperdiet dui. Cras molestie dignissim est, id congue nisi dictum a. Cras rutrum erat nec nulla commodo, efficitur rutrum orci aliquet. Suspendisse pellentesque, dui id aliquet suscipit, arcu velit sollicitudin enim, quis consequat tortor libero a turpis. Etiam luctus ligula est, non porta dolor tincidunt a. Etiam quis enim efficitur, finibus odio et, dictum nulla. Nullam fringilla massa at auctor ultrices. Phasellus ac mi consequat, aliquam lacus non, placerat neque. Suspendisse a rhoncus elit. Quisque ultricies libero et ipsum condimentum, sit amet euismod purus condimentum. Nullam euismod elit sed justo vulputate, et laoreet justo pharetra.              
            </Text>
          </View>
        </View>
      </ScrollView>      
    </TwoPaneView>
  );
};

const styles = StyleSheet.create({
  scrollView: {
    backgroundColor: 'white',
  },
  engine: {
    position: 'absolute',
    right: 0,
  },
  body: {
    backgroundColor: 'white',
  },
  sectionContainer: {
    backgroundColor: 'white',
    paddingHorizontal: 20,
    marginTop: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
    color: 'black',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
    color: 'black',
  },
  subSectionTitle: {
    fontSize: 18,
    fontWeight: '400',
    color: 'black',
  },
  subSectionDescription: {
    marginBottom: 12,
    fontSize: 14,
    fontWeight: '600',
    color: 'black',
  },
  bodyDescription: {
    marginTop: 8,
    fontSize: 14,
    fontWeight: '400',
    color: 'black',
  },
  highlight: {
    fontWeight: '700',
  },
  footer: {
    color: 'black',
    fontSize: 12,
    fontWeight: '600',
    padding: 4,
    paddingRight: 12,
    textAlign: 'right',
  },
});



export default App;
