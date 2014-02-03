package com.apple.itunes.com  ;

import com4j.*;

/**
 * IITPlaylistWindow Interface
 */
@IID("{349CBB45-2E5A-4822-8E4A-A75555A186F7}")
public interface IITPlaylistWindow extends com.apple.itunes.com.IITWindow {
  // Methods:
  /**
   * <p>
   * Returns a collection containing the currently selected track or tracks.
   * </p>
   * <p>
   * Getter method for the COM property "SelectedTracks"
   * </p>
   * @return  Returns a value of type com.apple.itunes.com.IITTrackCollection
   */

  @DISPID(1610809344) //= 0x60030000. The runtime will prefer the VTID if present
  @VTID(32)
  com.apple.itunes.com.IITTrackCollection selectedTracks();


  @VTID(32)
  @ReturnValue(defaultPropertyThrough={com.apple.itunes.com.IITTrackCollection.class})
  com.apple.itunes.com.IITTrack selectedTracks(
    int index);

  /**
   * <p>
   * The playlist displayed in the window.
   * </p>
   * <p>
   * Getter method for the COM property "Playlist"
   * </p>
   * @return  Returns a value of type com.apple.itunes.com.IITPlaylist
   */

  @DISPID(1610809345) //= 0x60030001. The runtime will prefer the VTID if present
  @VTID(33)
  com.apple.itunes.com.IITPlaylist playlist();


  // Properties:
}
