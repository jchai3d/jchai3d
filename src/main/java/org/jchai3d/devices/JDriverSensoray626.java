/*
 *   This file is part of the JCHAI 3D visualization and haptics libraries.
 *   Copyright (C) 2010 by JCHAI 3D. All rights reserved.
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License("GPL") version 2
 *   as published by the Free Software Foundation.
 *
 *   For using the JCHAI 3D libraries with software that can not be combined
 *   with the GNU GPL, and for taking advantage of the additional benefits
 *   of our support services, please contact CHAI 3D about acquiring a
 *   Professional Edition License.
 *
 *   project   <https://sourceforge.net/projects/jchai3d>
 *   version   1.0.0
 */


package org.jchai3d.devices;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.ShortByReference;

/**
 * offers an interface to the Sensoray 626 boards
 * @author jairo
 */
public class JDriverSensoray626 extends JGenericDevice
{
    
    //-----------------------------------------------------------------------
    // MEMBERS:
    //-----------------------------------------------------------------------

    /**
     * Handle to current board.
     */ 
    private int mBoardHandle;

    /** 
     * Number of handles that have been initialized
     */
    private static int mBOARD_NUM = 0;

    /**
     * Board base address
     */ 
    private short mWBaseAddress;

    /**
     * Initial values of the encoders to reset them
     */ 
    private long [] homeposition = new long[6];

    /**
     * Sensoray626 device communication
     */
    public static Sensoray626DeviceLibrary hlib = null;

    
    private static final int MAX_AXIS_626              = 6;
    private static final int NUM_DACs_626              = 4;
    private static final int DAC_RANGE_626             = 8191;
    private static final double VOLT_RANGE_626         = 10.0;

    //---------------------------------------------------------------------------
    // Error codes returned by GetErrors()
    //---------------------------------------------------------------------------
                                                                                                    // System errors that are not reset by S626_GetErrors():
    private static int ERR_OPEN              =0x00000001;         //   Can't open driver.
    private static int ERR_CARDREG           =0x00000002;         //   Can't attach driver to board.
    private static int ERR_ALLOC_MEMORY      =0x00000004;         //   Memory allocation error.
    private static int ERR_LOCK_BUFFER       =0x00000008;         //   Error locking DMA buffer.
    private static int ERR_THREAD            =0x00000010;         //   Error starting a thread.
    private static int ERR_INTERRUPT         =0x00000020;         //   Error enabling interrupt.
    private static int ERR_LOST_IRQ          =0x00000040;         //  Missed interrupt.
    private static int ERR_INIT              =0x00000080;         //  Board object not instantiated.
    private static int ERR_VERSION           =0x00000100;         //  Unsupported WinDriver version.
    private static int ERR_SUBIDS            =0x00000200;         //  PCI SubDevice/SubVendor ID mismatch.
    private static int ERR_CFGDUMP           =0x00000400;         //  PCI configuration dump failed.

    /**
     * Board errors that are reset by S626_GetErrors():
     */ 
    private static int ERR_ILLEGAL_PARM      =0x00010000;         //  Illegal function parameter value was specified.
    private static int ERR_I2C               =0x00020000;         //   I2C error.
    private static int ERR_DACTIMEOUT        =0x00100000;         //   DAC FB_BUFFER write timeout.
    private static int ERR_COUNTERSETUP      =0x00200000;         //   Illegal setup specified for counter channel.

    //---------------------------------------------------------------------------
    // ADC poll list constants
    //---------------------------------------------------------------------------

    private static int ADC_EOPL              =0x80;            // End-Of-Poll-List marker.
    private static int ADC_RANGE_10V         =0x00;            // Range code for ±10V range.
    private static int ADC_RANGE_5V          =0x10;            // Range code for ±5V range.
    private static int ADC_CHANMASK          =0x0F;            // Channel number mask.

    //---------------------------------------------------------------------------
    // Counter constants
    //---------------------------------------------------------------------------

    /**
     * LoadSrc values:
     */ 
    private static int LOADSRC_INDX          =0;               //   Preload core in response to Index.
    private static int LOADSRC_OVER          =1;               //   Preload core in response to Overflow.
    private static int LOADSRCB_OVERA        =2;               //   Preload B core in response to A Overflow.
    private static int LOADSRC_NONE          =3;               //   Never preload core.

    /**
     * IntSrc values:
     */ 
    private static int INTSRC_NONE           =0;               //   Interrupts disabled.
    private static int INTSRC_OVER           =1;               //   Interrupt on Overflow.
    private static int INTSRC_INDX           =2;               //   Interrupt on Index.
    private static int INTSRC_BOTH           =3;               //   Interrupt on Index or Overflow.

    /**
     * LatchSrc values:
     */ 
    private static int LATCHSRC_AB_READ      =0;               //   Latch on read.
    private static int LATCHSRC_A_INDXA      =1;               //   Latch A on A Index.
    private static int LATCHSRC_B_INDXB      =2;               //   Latch B on B Index.
    private static int LATCHSRC_B_OVERA      =3;               //   Latch B on A Overflow.

    /**
     * IndxSrc values:
     */ 
    private static int INDXSRC_HARD          =0;               //   Hardware or software index.
    private static int INDXSRC_SOFT          =1;               //   Software index only.

    /**
     * IndxPol values:
     */ 
    private static int INDXPOL_POS           =0;               //   Index input is active high.
    private static int INDXPOL_NEG           =1;               //   Index input is active low.

    /**
     * ClkSrc values:
     */ 
    private static int CLKSRC_COUNTER        =0;               //   Counter mode.
    private static int CLKSRC_TIMER          =2;               //   Timer mode.
    private static int CLKSRC_EXTENDER       =3;               //   Extender mode.

    /**
     * ClkPol values:
     */ 
    private static int CLKPOL_POS            =0;               //   Counter/Extender clock is active high.
    private static int CLKPOL_NEG            =1;               //   Counter/Extender clock is active low.
    private static int CNTDIR_UP             =0;               //   Timer counts up.
    private static int CNTDIR_DOWN           =1;               //   Timer counts down.

    /**
     * ClkEnab values:
     */ 
    private static int CLKENAB_ALWAYS        =0;               //   Clock always enabled.
    private static int CLKENAB_INDEX         =1;               //   Clock is enabled by index.

    /**
     * ClkMult values: 
     */
    private static int CLKMULT_4X            =0;               //   4x clock multiplier.
    private static int CLKMULT_2X            =1;               //   2x clock multiplier.
    private static int CLKMULT_1X            =2;               //   1x clock multiplier.

    /**
     * Bit Field positions in COUNTER_SETUP structure:
     */ 
    private static int BF_LOADSRC            =9;               //   Preload trigger.
    private static int BF_INDXSRC            =7;               //   Index source.
    private static int BF_INDXPOL            =6;               //   Index polarity.
    private static int BF_CLKSRC             =4;               //   Clock source.
    private static int BF_CLKPOL             =3;               //   Clock polarity/count direction.
    private static int BF_CLKMULT            =1;               //   Clock multiplier.
    private static int BF_CLKENAB            =0;               //   Clock enable.

    /**
     * Counter channel numbers:
     */ 
    private static int CNTR_0A               =0;               //   Counter 0A.
    private static int CNTR_1A               =1;               //   Counter 1A.
    private static int CNTR_2A               =2;               //   Counter 2A.
    private static int CNTR_0B               =3;               //   Counter 0B.
    private static int CNTR_1B               =4;               //   Counter 1B.
    private static int CNTR_2B               =5;               //   Counter 2B.

    /**
     * Error codes returned by S626_DLLOpen().
     */
    private static int ERR_LOAD_DLL          =1;               // Failed to open S626.DLL.
    private static int ERR_FUNCADDR          =2;               // Failed to find function name in S626.DLL.

    //-----------------------------------------------------------------------
    // CONSTRUCTOR:
    //-----------------------------------------------------------------------

    /**
     * Constructor of JDriverSensoray626.
     */
    public JDriverSensoray626()
    {
        mBoardHandle = mBOARD_NUM;
        mBOARD_NUM++;
        mSystemReady = false;
        mSystemAvailable = false;
    }

    //-----------------------------------------------------------------------
    // METHODS:
    //-----------------------------------------------------------------------

    /**
     * Counter overflow/index event flag masks for S626_CounterCapStatus()
     * and S626_InterruptStatus().
     */ 
    private int INDXMASK(int C)
    {
        return ( 1 << ( ( (C) > 2 ) ? ( (C) * 2 - 1 ) : ( (C) * 2 +  4 ) ) );
    }

    private int OVERMASK(int C)
    {
        return ( 1 << ( ( (C) > 2 ) ? ( (C) * 2 + 5 ) : ( (C) * 2 + 10 ) ) );
    }

    //---------------------------------------------------------------------------
    // Open S626.DLL and get pointers to exported DLL functions.
    //---------------------------------------------------------------------------

    private int S626_DLLOpen()
    {

        // Dynamically link to S626.DLL, exit with error if link failed.
        try
        {
            hlib =
                    (Sensoray626DeviceLibrary) Native.loadLibrary(
                    "S626",
                    Sensoray626DeviceLibrary.class);

            // check if file exists
            if(hlib == null)
                throw(new Exception("Falcon device not loaded!"));

        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ERR_LOAD_DLL;
        }

        // Fill pointers to S626.DLL functions, exit with error if attempt failed.
	/*if ( GetFuncPtrs() )
	{
            FreeLibrary( hlib );
            return ERR_FUNCADDR;
	}
        */

	// Normal return.
	return 0;
    }

    /**
     * Sets all counters on the board to be used as encoders.
     */
    private void encoderInit()
    {
        // Initialize all encoders at once
        for (int i = 0; i<6; i++)
        {
            try{
                hlib.S626_CounterModeSet(mBoardHandle, (short)i,(short)(
                ( LOADSRC_INDX		<<	BF_LOADSRC	) |
                ( INDXSRC_SOFT		<<	BF_INDXSRC	) |
                ( INDXPOL_POS		<<	BF_INDXPOL	) |
                ( CLKSRC_COUNTER	<<	BF_CLKSRC	) |
                ( CLKPOL_POS		<<	BF_CLKPOL	) |
                ( CLKMULT_4X		<<	BF_CLKMULT	) |
                ( CLKENAB_ALWAYS	<<	BF_CLKENAB	)) );

                // Set the counter CNTR_0A on BOARD to 0
                hlib.S626_CounterPreload( mBoardHandle, (short)i, 100000 );
                hlib.S626_CounterLoadTrigSet(mBoardHandle, (short)i, (short)1);
                hlib.S626_CounterLatchSourceSet( mBoardHandle, (short)i, (short)LATCHSRC_AB_READ );
                hlib.S626_CounterIntSourceSet( mBoardHandle, (short)i, (short)INTSRC_INDX );
                homeposition[i] = hlib.S626_CounterReadLatch(mBoardHandle, (short)i);
            }
            catch(Exception e)
            {e.printStackTrace();}
        }
    }
    
    /**
     * Open connection to Sensoray board
     * @return - Return 0 is operation succeeds, -1 if an error occurs.
     */
    @Override
    public int open()
    {
        // check if device is not already opened
        if (mSystemReady) { return (0); }

        // number of encoders used.
        for (int i = 0; i<MAX_AXIS_626; i++)
        {
            homeposition[i] = 0;
        }

        // open DLL to talk to the board
        if (S626_DLLOpen()==0)
        {
            mSystemAvailable = true;

            // Declare Model 626 board to driver and launch the interrupt thread.
            // NOTE: we're supposing to only use one board. With two board we need
            // to specify the physical address for each board used.
            // ALSO: we're NOT using interrupts from the board.
            
            long ErrCode = -1;
            try{
                hlib.S626_OpenBoard( mBoardHandle, 0, null, 1 );
                ErrCode = hlib.S626_GetErrors( 0 );
            }
            catch(Exception e)
            {e.printStackTrace();}
            if (ErrCode != 0)
            {
                mSystemReady = false;
                return (-1);
            }
            else
            {
                mSystemReady = true;
            }

            mWBaseAddress = 0;
            encoderInit();
            for (int i = 0; i<NUM_DACs_626; i++)
            {
                try{
                    hlib.S626_WriteDAC(mBoardHandle, (short)i, 0);
                }
                catch(Exception e)
                {e.printStackTrace();}
            }
        }
        else
        {
            mSystemReady = false;
            mSystemAvailable = false;
            return (-1);
        }

        return (0);
    }

    /**
     * Close connection to the board, write a zero value to all DACs.
     * @return - Return 0 is operation succeeds, -1 if an error occurs.
     */
    @Override
    public int close()
    {
        if (mSystemReady)
        {
            for (int i = 0; i<NUM_DACs_626; i++)
            {
                try{
                    hlib.S626_WriteDAC(mBoardHandle, (short) i, 0);
                }
                catch(Exception e)
                {e.printStackTrace();}
            }

            try{
                hlib.S626_CloseBoard(mBoardHandle);
            }
            catch(Exception e)
            {e.printStackTrace();}            
        }

        mSystemReady = false;
        mSystemAvailable = false;

        return (0);
    }

    /**
     * Initializes board. In this implementation there's really nothing to do
     * that hasn't been done in the opening phase.
     * @return - Return 0 is operation succeeds, -1 if an error occurs.
     */
    public int initialize()
    {
        return initialize(false);
    }

    /**
     * Initializes board. In this implementation there's really nothing to do
     * that hasn't been done in the opening phase.
     * @param aResetEncoders - Ignored; reserved for forward compatibility
     * @return - Return 0 is operation succeeds, -1 if an error occurs.
     */
    @Override
    public int initialize(final boolean aResetEncoders)
    {
        if (mSystemReady)
        {
            return (0);
        }
        else
        {
            return (-1);
        }
    }

    /**
     * Set command to the Sensoray626 board.
     * possible commands are:
     * CHAI_CMD_GET_DEVICE_STATE: returns an int (1 board is ready, 0 board is NOT ready)
     * CHAI_CMD_GET_ENCODER_0: reads encoder 0, returns counts value in a long
     * CHAI_CMD_GET_ENCODER_1: reads encoder 1, returns counts value in a long
     * CHAI_CMD_GET_ENCODER_2: reads encoder 2, returns counts value in a long
     * CHAI_CMD_GET_ENCODER_3: reads encoder 3, returns counts value in a long
     * CHAI_CMD_GET_ENCODER_4: reads encoder 4, returns counts value in a long
     * CHAI_CMD_GET_ENCODER_5: reads encoder 5, returns counts value in a long
     * CHAI_CMD_SET_DAC_0: writes a voltage to DAC 0 a value between +10 and -10 volts, which is a double
     * CHAI_CMD_SET_DAC_1: writes a voltage to DAC 1 a value between +10 and -10 volts, which is a double
     * CHAI_CMD_SET_DAC_2: writes a voltage to DAC 2 a value between +10 and -10 volts, which is a double
     * CHAI_CMD_SET_DAC_3: writes a voltage to DAC 3 a value between +10 and -10 volts, which is a double
     * @param aCommand - Selected command.
     * @param aData - Pointer to the corresponding data structure.
     * @return - Return status of command.
     */
    @Override
    public int command(int aCommand, Object aData)
    {
        int retval = CHAI_MSG_OK;
        if (mSystemReady)
        {
            try{
               switch (aCommand)
               {
                   case CHAI_CMD_GET_DEVICE_STATE:
                   {
                       aData = mSystemReady;
                   }
                   break;
                   // reset encoder 0
                   case CHAI_CMD_RESET_ENCODER_0:
                   {
                       // Read in encoder positions here
                       long cur_pos;
                       cur_pos = hlib.S626_CounterReadLatch((long)mBoardHandle, (short)0);
                       homeposition[0] = cur_pos;
                   }
                   break;
                   // reset encoder 1
                   case CHAI_CMD_RESET_ENCODER_1:
                   {
                       // Read in encoder positions here
                       long cur_pos;
                       cur_pos = hlib.S626_CounterReadLatch((long)mBoardHandle, (short)1);
                       homeposition[1] = cur_pos;
                   }
                   break;
                   // reset encoder 2
                   case CHAI_CMD_RESET_ENCODER_2:
                   {
                       // Read in encoder positions here
                       long cur_pos;
                       cur_pos = hlib.S626_CounterReadLatch((long)mBoardHandle, (short)2);
                       homeposition[2] = cur_pos;
                   }
                   break;
                   // reset encoder 3
                   case CHAI_CMD_RESET_ENCODER_3:
                   {
                       // Read in encoder positions here
                       long cur_pos;
                       cur_pos = hlib.S626_CounterReadLatch((long)mBoardHandle, (short)3);
                       homeposition[3] = cur_pos;
                   }
                   break;
                   // reset encoder 4
                   case CHAI_CMD_RESET_ENCODER_4:
                   {
                       // Read in encoder positions here
                       long cur_pos;
                       cur_pos = hlib.S626_CounterReadLatch((long)mBoardHandle, (short)4);
                       homeposition[4] = cur_pos;
                   }
                   break;
                   // reset encoder 5
                   case CHAI_CMD_RESET_ENCODER_5:
                   {
                       // Read in encoder positions here
                       long cur_pos;
                       cur_pos = hlib.S626_CounterReadLatch((long)mBoardHandle, (short)5);
                       homeposition[5] = cur_pos;
                   }
                   break;
                   // read encoder 0
                   case CHAI_CMD_GET_ENCODER_0:
                   {
                       long cur_pos;
                       // Read in encoder positions here
                       cur_pos = hlib.S626_CounterReadLatch((long)mBoardHandle, (short)0) - homeposition[0];
                       aData = cur_pos;
                   }
                   break;
                   // read encoder 1
                   case CHAI_CMD_GET_ENCODER_1:
                   {
                       long cur_pos;
                       // Read in encoder positions here
                       cur_pos = hlib.S626_CounterReadLatch((long)mBoardHandle, (short)1) - homeposition[1];
                       aData = cur_pos;
                   }
                   break;
                   // read encoder 2
                   case CHAI_CMD_GET_ENCODER_2:
                   {
                       long cur_pos;
                       // Read in encoder positions here
                       cur_pos = hlib.S626_CounterReadLatch((long)mBoardHandle, (short)2) - homeposition[2];
                       aData = cur_pos;
                   }
                   break;
                   // read encoder 3
                   case CHAI_CMD_GET_ENCODER_3:
                   {
                       long cur_pos;
                       // Read in encoder positions here
                       cur_pos = hlib.S626_CounterReadLatch((long)mBoardHandle, (short)3) - homeposition[3];
                       aData = cur_pos;
                   }
                   break;
                   // read encoder 4
                   case CHAI_CMD_GET_ENCODER_4:
                   {
                       long cur_pos;
                       // Read in encoder positions here
                       cur_pos = hlib.S626_CounterReadLatch((long)mBoardHandle, (short)4) - homeposition[4];
                       aData = cur_pos;
                   }
                   break;
                   // read encoder 5
                   case CHAI_CMD_GET_ENCODER_5:
                   {
                       long cur_pos;
                       // Read in encoder positions here
                       cur_pos = hlib.S626_CounterReadLatch((long)mBoardHandle, (short)5) - homeposition[5];
                       aData = cur_pos;
                   }
                   break;
                   // write motor 0
                   case CHAI_CMD_SET_DAC_0:
                   {
                       short lCounts;
                       double iVolts = (Double) aData;
                       if (iVolts> VOLT_RANGE_626)
                            aData = iVolts = VOLT_RANGE_626;
                       if (iVolts < -VOLT_RANGE_626)
                            aData = iVolts = -VOLT_RANGE_626;
                       // convert value from volts to a value between -DAC_RANGE_626 and DAC_RANGE_626
                       lCounts = (short) ((double) DAC_RANGE_626 * (iVolts/VOLT_RANGE_626));
                       hlib.S626_WriteDAC((long)mBoardHandle, (short) 0, lCounts);
                    }
                    break;
                    // write motor 1
                   case CHAI_CMD_SET_DAC_1:
                   {
                       short lCounts;
                       double iVolts = (Double) aData;
                       if (iVolts> VOLT_RANGE_626)
                            aData = iVolts = VOLT_RANGE_626;
                       if (iVolts < -VOLT_RANGE_626)
                            aData = iVolts = -VOLT_RANGE_626;
                       // convert value from volts to a value between -DAC_RANGE_626 and DAC_RANGE_626
                       lCounts = (short) ((double) DAC_RANGE_626 * (iVolts/VOLT_RANGE_626));
                       hlib.S626_WriteDAC((long)mBoardHandle, (short) 1, lCounts);
                    }
                    break;
                   // write motor 2
                   case CHAI_CMD_SET_DAC_2:
                   {
                       short lCounts;
                       double iVolts = (Double) aData;
                       if (iVolts> VOLT_RANGE_626)
                            aData = iVolts = VOLT_RANGE_626;
                       if (iVolts < -VOLT_RANGE_626)
                            aData = iVolts = -VOLT_RANGE_626;
                       // convert value from volts to a value between -DAC_RANGE_626 and DAC_RANGE_626
                       lCounts = (short) ((double) DAC_RANGE_626 * (iVolts/VOLT_RANGE_626));
                       hlib.S626_WriteDAC((long)mBoardHandle, (short) 2, lCounts);
                    }
                    break;
                    // write motor 3
                    case CHAI_CMD_SET_DAC_3:
                    {
                       short lCounts;
                       double iVolts = (Double) aData;
                       if (iVolts> VOLT_RANGE_626)
                            aData = iVolts = VOLT_RANGE_626;
                       if (iVolts < -VOLT_RANGE_626)
                            aData = iVolts = -VOLT_RANGE_626;
                       // convert value from volts to a value between -DAC_RANGE_626 and DAC_RANGE_626
                       lCounts = (short) ((double) DAC_RANGE_626 * (iVolts/VOLT_RANGE_626));
                       hlib.S626_WriteDAC((long)mBoardHandle, (short) 3, lCounts);
                    }
                    break;
                   // function is not implemented for phantom devices
                   default:
                    retval = CHAI_MSG_NOT_IMPLEMENTED;
                }
            }catch(Exception e){
                retval = CHAI_MSG_ERROR;
                e.printStackTrace();
            }
        }
        else
        {
            retval = CHAI_MSG_ERROR;
        }
        return retval;
    }

    interface Sensoray626DeviceLibrary extends Library
    {
        //---------------------------------------------------------------------------
        // Methods that are exported by S626.DLL.
        //---------------------------------------------------------------------------

        // Status and control functions.
        public long    S626_GetAddress              (long hbd );
        public long    S626_GetErrors               (long hbd );
        public void    S626_OpenBoard               (long hbd,
                                                        long PhysLoc,
                                                        CallbackFPTR_ISR IntFunc,
                                                        long Priority );
        public void    S626_CloseBoard              (long hbd );
        public void    S626_InterruptEnable         (long hbd,
                                                        short enable );
        public void    S626_InterruptStatus         (long hbd,
                                                        ShortByReference status );
        public void    S626_SetErrCallback          (long hbd,
                                                        CallbackFPTR_ERR Callback );

        // Diagnostics.
        public char    S626_I2CRead                 (long hbd,
                                                        char addr );
        public void    S626_I2CWrite                (long hbd,
                                                        char addr,
                                                        char value );
        public short   S626_RegRead                 (long hbd,
                                                        short addr );
        public void    S626_RegWrite                (long hbd,
                                                        short addr,
                                                        short value );

        // Analog I/O functions.
        public void    S626_ReadADC                 (long hbd,
                                                        ShortByReference databuf );
        public void    S626_ResetADC                (long hbd,
                                                        String pollist );
        public void    S626_StartADC                (long hbd);
        public void    S626_WaitDoneADC             (long hbd,
                                                        ShortByReference pdata);
        public void    S626_WriteDAC                (long hbd,
                                                        short chan,
                                                        long value );
        public void    S626_WriteTrimDAC            (long hbd,
                                                        char chan,
                                                        char value );

        // Digital I/O functions.
        public short   S626_DIOReadBank             (long hbd,
                                                        short group );
        public short   S626_DIOWriteBankGet         (long hbd,
                                                        short group );
        public void    S626_DIOWriteBankSet         (long hbd,
                                                        short group,
                                                        short value );
        public short   S626_DIOEdgeGet              (long hbd,
                                                        short group );
        public void    S626_DIOEdgeSet              (long hbd,
                                                        short group,
                                                        short value );
        public void    S626_DIOCapEnableSet         (long hbd,
                                                        short group,
                                                        short chanmask,
                                                        short enable );
        public short   S626_DIOCapEnableGet         (long hbd,
                                                        short group );
        public void    S626_DIOCapStatus            (long hbd,
                                                        short group );
        public void    S626_DIOCapReset             (long hbd,
                                                        short group,
                                                        short value );
        public short   S626_DIOIntEnableGet         (long hbd,
                                                        short group );
        public void    S626_DIOIntEnableSet         (long hbd,
                                                        short group,
                                                        short value );
        public short   S626_DIOModeGet              (long hbd,
                                                        short group );
        public void    S626_DIOModeSet              (long hbd,
                                                        short group,
                                                        short value );

        // Counter functions.
        public void    S626_CounterModeSet          (long hbd,
                                                        short chan,
                                                        short mode );
        public short   S626_CounterModeGet          (long hbd,
                                                        short chan );
        public void    S626_CounterEnableSet        (long hbd,
                                                        short chan,
                                                        short enable );
        public void    S626_CounterPreload          (long hbd,
                                                        short chan,
                                                        long value );
        public void    S626_CounterLoadTrigSet      (long hbd,
                                                        short chan,
                                                        short value );
        public void    S626_CounterLatchSourceSet   (long hbd,
                                                        short chan,
                                                        short value );
        public long    S626_CounterReadLatch        (long hbd,short chan );
        public short   S626_CounterCapStatus        (long hbd );
        public void    S626_CounterCapFlagsReset    (long hbd,
                                                        short chan );
        public void    S626_CounterSoftIndex        (long hbd,
                                                        short chan );
        public void    S626_CounterIntSourceSet     (long hbd,
                                                        short chan,
                                                        short value );

        // Battery functions:
        public short   S626_BackupEnableGet         (long hbd );
        public void    S626_BackupEnableSet         (long hbd,
                                                        short en );
        public short   S626_ChargeEnableGet         (long hbd );
        public void    S626_ChargeEnableSet         (long hbd,
                                                        short en );

        // Watchdog functions:
        public short   S626_WatchdogTimeout         (long hbd );
        public short   S626_WatchdogEnableGet       (long hbd );
        public void    S626_WatchdogEnableSet       (long hbd,
                                                        short en );
        public short   S626_WatchdogPeriodGet       (long hbd );
        public void    S626_WatchdogPeriodSet       (long hbd,
                                                        short val );
        public void    S626_WatchdogReset           (long hbd );

        //---------------------------------------------------------------------------
        // Special interfaces for callbacks
        //---------------------------------------------------------------------------

        interface CallbackFPTR_ERR extends Callback
        {
            void FPTR_ERR (long ErrFlags);
        }

        interface CallbackFPTR_ISR extends Callback
        {
            void FPTR_ISR ();
        }

    }

}
