package com.apple.itunes.com  ;

import com4j.*;

/**
 * IITSource Interface
 */
@IID("{AEC1C4D3-AEF1-4255-B892-3E3D13ADFDF9}")
public interface IITSource extends com.apple.itunes.com.IITObject {
  // Methods:
  /**
   * <p>
   * The source kind.
   * </p>
   * <p>
   * Getter method for the COM property "Kind"
   * </p>
   * @return  Returns a value of type com.apple.itunes.com.ITSourceKind
   */

  @DISPID(1610809344) //= 0x60030000. The runtime will prefer the VTID if present
  @VTID(15)
  com.apple.itunes.com.ITSourceKind kind();


  /**
   * <p>
   * The total size of the source, if it has a fixed size.
   * </p>
   * <p>
   * Getter method for the COM property "Capacity"
   * </p>
   * @return  Returns a value of type double
   */

  @DISPID(1610809345) //= 0x60030001. The runtime will prefer the VTID if present
  @VTID(16)
  double capacity();


  /**
   * <p>
   * The free space on the source, if it has a fixed size.
   * </p>
   * <p>
   * Getter method for the COM property "FreeSpace"
   * </p>
   * @return  Returns a value of type double
   */

  @DISPID(1610809346) //= 0x60030002. The runtime will prefer the VTID if present
  @VTID(17)
  double freeSpace();


  /**
   * <p>
   * Returns a collection of playlists.
   * </p>
   * <p>
   * Getter method for the COM property "Playlists"
   * </p>
   * @return  Returns a value of type com.apple.itunes.com.IITPlaylistCollection
   */

  @DISPID(1610809347) //= 0x60030003. The runtime will prefer the VTID if present
  @VTID(18)
  com.apple.itunes.com.IITPlaylistCollection playlists();


  @VTID(18)
  @ReturnValue(defaultPropertyThrough={com.apple.itunes.com.IITPlaylistCollection.class})
  com.apple.itunes.com.IITPlaylist playlists(
    int index);

  // Properties:
}
