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
} from 'react-native';

const TPV_Orientation = {
  Horizontal: 'horizontal',
  Vertical: 'vertical',
};

const TPV_PanePriority = {
  PaneA: 'paneA',
  PaneB: 'paneB',
};

export default class TwoPaneView extends Component {
  state = {
    dims: Dimensions.get('window'),
    displayMask: Dimensions.get('displayMask'),
    spanning: Dimensions.get('displayMask').isAppSpanned,
    panePriority: this.props.panePriority,
    panePriorityVerticalSpanning: this.props.panePriorityVerticalSpanning,
  };

  componentDidMount() {
    Dimensions.addEventListener('change', this._handleDimensionsChange);
  }

  componentWillUnmount() {
    Dimensions.removeEventListener('change', this._handleDimensionsChange);
  }

  _handleDimensionsChange = dimensions => {
    this.setState({
      dims: dimensions.window,
      displayMask: dimensions.displayMask,
      spanning: dimensions.displayMask.isAppSpanned
    });
  };

  render() {
/*    
    return (
      <View style={{width: this.state.dims.width, height:this.state.dims.height, borderColor: 'red', borderWidth: 25}}>
        {this.renderChildPanes()}
      </View>
    );
*/    

    let direction =
      this.state.dims.width >= this.state.dims.height ? 'row' : 'column';

    return (
      <View style={{flexDirection: direction, width: this.state.dims.width, height:this.state.dims.height, backgroundColor:'white'}}>
        {this.renderChildPanes()}
      </View>
    );
  }

  renderChildPanes() {
    const children = React.Children.toArray(this.props.children);

    if (this.state.spanning) {
      if (this.state.dims.height > this.state.dims.width  && this.state.panePriorityVerticalSpanning) {
        if (this.state.panePriorityVerticalSpanning === TPV_PanePriority.PaneA) {
          return this.renderPaneA(this.getEntireSize());
        }
        else {
          return this.renderPaneB(this.getEntireSize());
        }
      }
      return this.renderBothPanes();
    }
    if (this.state.panePriority === TPV_PanePriority.PaneA) {
      return this.renderPaneA(this.getEntireSize());
    }
    return this.renderPaneB(this.getEntireSize());
  }

  renderBothPanes() {
    let horizontal = this.state.dims.width >= this.state.dims.height;
    const children = React.Children.toArray(this.props.children);

    const items = [];
    if (children.length > 0) {
      items.push(this.renderPaneA(horizontal ? this.getLeftSize() : this.getTopSize()));
    }

    items.push(this.renderSeparator());

    if (children.length > 1) {
      items.push(this.renderPaneB(horizontal ? this.getRightSize() : this.getBottomSize()));
    }

    return items;
  }

  renderPaneA(size) {
    const children = React.Children.toArray(this.props.children);
    if (children.length > 0) {
      return (
        <View key="paneA" style={{width:size.width, height:size.height}}>
          {children[0]}
        </View>
      );
    }
  }

  renderPaneB(size) {
    const children = React.Children.toArray(this.props.children);
    if (children.length > 1) {
      return (
        <View key="paneB" style={{width:size.width, height:size.height}}>
          {children[1]}
        </View>
      );
    }
  }

  renderSeparator() {
    let horizontal = this.state.dims.width >= this.state.dims.height;
    let separatorWidth = horizontal ? this.state.displayMask.width : '100%';
    let separatorHeight = horizontal ? '100%' : this.state.displayMask.height;
    return (
      <View
        key="separator"
        style={{width: separatorWidth, height: separatorHeight}}
      />
    );
  }

  getEntireSize() {
    var size = {
      width: this.state.dims.width,
      height: this.state.dims.height,
    };
    return size;
  }

  getTopSize() {
    let topHeight = this.state.displayMask.top > 0 ? this.state.displayMask.top : this.state.dims.height/2;
    var size = {
      width: this.state.dims.width,
      height: topHeight,
    }
    return size;
  }

  getBottomSize() {
    let topHeight = this.state.displayMask.bottom > 0 ? this.state.dims.height - this.state.displayMask.bottom : this.state.dims.height/2;
    var size = {
      width: this.state.dims.width,
      height: topHeight,
    }
    return size;
  }

  getLeftSize() {
    let leftWidth = this.state.displayMask.left > 0 ? this.state.displayMask.left : this.state.dims.width/2;
    var size = {
      width: leftWidth,
      height: this.state.dims.height,
    }
    return size;
  }

  getRightSize() {
    let rightWidth = this.state.displayMask.right > 0 ? this.state.dims.width - this.state.displayMask.right : this.state.dims.width/2;
    var size = {
      width: rightWidth,
      height: this.state.dims.height,
    }
    return size;
  }
}

const styles = StyleSheet.create({
    twoPaneView: {
      flexDirection: 'row',
    },
    paneA: {
      backgroundColor: 'green',
    },
    paneB: {
      backgroundColor: 'blue',
    },
  });
  