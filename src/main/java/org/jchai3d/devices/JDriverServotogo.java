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

import com.sun.jna.Union;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.ShortByReference;

/**
 * offers an interface to the Servo2Go boards.
 * @author jairo
 */
public class JDriverServotogo extends JGenericDevice 
{

    //-----------------------------------------------------------------------
    // MEMBERS:
    //-----------------------------------------------------------------------

    /**
     * Initial values of the encoder to reset them.
     */
    private long [] mHomeposition = new long [20];

    /**
     * Board base address.
     */
    private short mWBaseAddress;

    /**
     * Interrupt request used by board.
     */
    private short mWIrq;

    /**
     * Board model: defined by BrdtstOK.
     */
    private short mWModel;

    /**
     * Tells if the board is present. set by stg_init()
     */
    private short mWNoBoardFlag;

    /**
     * Number of encoders used. set by stgInit() through encoderInit().
     */
    private short mWAxesInSys;

    /**
     * Directions for DIO. Set by DIODirections(). (don't care now).
     */
    private short mWSaveDirs;

    /**
     * Set by stg_init()
     */
    private char  mByIndexPollAxis;

    /**
     * Polarity of signals.
     */
    private char  mByIndexPulsePolarity;

    
    private static DlPortAPI dlPort = null;

    
    private static final int CNT0_D     =0x00;
    private static final int CNT1_D     =0x01;
    private static final int CNT0_C     =0x02;
    private static final int CNT1_C     =0x03;
    private static final int CNT2_D     =0x04;
    private static final int CNT3_D     =0x05;
    private static final int CNT2_C     =0x06;
    private static final int CNT3_C     =0x07;
    private static final int CNT4_D     =0x08;
    private static final int CNT5_D     =0x09;
    private static final int CNT4_C     =0x0a;
    private static final int CNT5_C     =0x0b;
    private static final int CNT6_D     =0x0c;
    private static final int CNT7_D     =0x0d;
    private static final int CNT6_C     =0x0e;
    private static final int CNT7_C     =0x0f;
    private static final int DAC_0      =0x10;
    private static final int DAC_1      =0x12;
    private static final int DAC_2      =0x14;
    private static final int DAC_3      =0x16;
    private static final int DAC_4      =0x18;
    private static final int DAC_5      =0x1a;
    private static final int DAC_6      =0x1c;
    private static final int DAC_7      =0x1e;
    private static final int ADC        =0x410;
    private static final int ADC_0      =0x410;
    private static final int ADC_1      =0x412;
    private static final int ADC_2      =0x414;
    private static final int ADC_3      =0x416;
    private static final int ADC_4      =0x418;
    private static final int ADC_5      =0x41a;
    private static final int ADC_6      =0x41c;
    private static final int ADC_7      =0x41e;
    private static final int CNTRL0     =0x401;
    private static final int DIO_A      =0x400;
    private static final int DIO_B      =0x402;
    private static final int DIO_C      =0x404;
    private static final int DIO_D      =0x401;
    private static final int PORT_A     =0x400;
    private static final int PORT_B     =0x402;
    private static final int PORT_C     =0x404;
    private static final int PORT_D     =0x405;
    private static final int INTC       =0x405;
    private static final int BRDTST     =0x403;
    private static final int MIO_1      =0x406;
    private static final int ABC_DIR    =0x406;
    private static final int MIO_2      =0x407;
    private static final int D_DIR      =0x407;
    private static final int ODDRST     =0x407;
    private static final int TIMER_0    =0x408;
    private static final int TIMER_1    =0x40a;
    private static final int TIMER_2    =0x40c;
    private static final int TMRCMD     =0x40e;
    private static final int ICW1       =0x409;
    private static final int ICW2       =0x40b;
    private static final int OCW1       =0x40b;
    private static final int OCW2       =0x409;
    private static final int OCW3       =0x409;
    private static final int IRRreg     =0x409;
    private static final int ISR        =0x409;
    private static final int IDLEN      =0x409;
    private static final int IMR        =0x40b;
    private static final int SELDI      =0x40b;
    private static final int IDL        =0x40d;
    private static final int CNTRL1     =0x40f;

    //---------------------------------------------------------------------------
    // Some bit masks for various registers
    //---------------------------------------------------------------------------

    /**
     * for IRR, ISR, and IMR
     */
    private static final int IXEVN      =0x80;
    private static final int IXODD      =0x40;
    private static final int LIXEVN     =0x20;
    private static final int LIXODD     =0x10;
    private static final int EOC        =0x08;
    private static final int TP0        =0x04;
    private static final int USR_INT    =0x02;
    private static final int TP2        =0x01;

    /**
     * for INTC
     */
    private static final int AUTOZERO   =0x80;
    private static final int IXLVL      =0x40;
    private static final int IXS1       =0x20;
    private static final int IXS0       =0x10;
    private static final int USRINT     =0x08;
    private static final int IA2        =0x04;
    private static final int IA1        =0x02;
    private static final int IA0        =0x01;

    private static final int CNTRL0_AZ   =0x80;
    private static final int CNTRL0_AD2  =0x40;
    private static final int CNTRL0_AD1  =0x20;
    private static final int CNTRL0_AD0  =0x10;
    private static final int CNTRL0_CAL  =0x08;
    private static final int CNTRL0_IA2  =0x04;
    private static final int CNTRL0_IA1  =0x02;
    private static final int CNTRL0_IA0  =0x01;

    private static final int CNTRL1_WDTOUT    =0x80;
    private static final int CNTRL1_INT_G2    =0x40;
    private static final int CNTRL1_INT_T0    =0x10;
    private static final int CNTRL1_INT_T2    =0x20;
    private static final int CNTRL1_NOT_SLAVE =0x08;
    private static final int CNTRL1_IEN_G2    =0x04;
    private static final int CNTRL1_IEN_T0    =0x01;
    private static final int CNTRL1_IEN_T2    =0x02;

    private static final int BRDTST_EOC       =0x08;
    private static final int IRQSL            =0x84;

    //---------------------------------------------------------------------------
    // Hardware direction bit definitions
    //---------------------------------------------------------------------------

    private static final int A_DIR_BIT      =0x10;
    private static final int B_DIR_BIT      =0x02;
    private static final int C_LOW_DIR_BIT  =0x01;
    private static final int C_HI_DIR_BIT   =0x08;
    private static final int D_DIR_BIT      =0x10;
    private static final int D_LOW_DIR_BIT  =0x01;
    private static final int D_HI_DIR_BIT   =0x08;

    //---------------------------------------------------------------------------
    // Parameters to the SelectInterruptPeriod Command
    //---------------------------------------------------------------------------

    private static final int _500_MICROSECONDS        =500;
    private static final int _1_MILLISECOND           =1000;
    private static final int _2_MILLISECONDS          =2000;
    private static final int _3_MILLISECONDS          =3000;
    private static final int _4_MILLISECONDS          =4000;
    private static final int _5_MILLISECONDS          =5000;
    private static final int _10_MILLISECONDS         =10000;
    private static final long _100_MILLISECONDS       =100000L;
    private static final long _1_SECOND               =1000000L;
    private static final long MAX_PERIOD              =-1L;

    private static final int NO_BOARD       =1;
    private static final int BOARD_PRESENT  =0;

    private static final int MODEL_NO_ID    =0;
    private static final int MODEL1         =1;
    private static final int MODEL2         =2;

    private static final int MAX_AXIS = 8;
    private static final int DAC_RANGE_STG = 4095;
    private static final double VOLT_RANGE_STG = 10.0;

    //-----------------------------------------------------------------------
    // CONSTRUCTOR:
    //-----------------------------------------------------------------------

    /**
     * Constructor of JDriverServotogo.
     */
    public JDriverServotogo()
    {
        mSystemReady = false;
        mSystemAvailable = false;
        mWAxesInSys = 0;
        mWIrq = 5;
    }

    //-----------------------------------------------------------------------
    // METHODS:
    //-----------------------------------------------------------------------

    /**
     * Set volt value dVolts for the i-th dac channel. If a_num is out of the range
     * of DACs for the board no action is taken.
     * @param aNum - dac number.
     * @param aVolts - value to be applied to dacs
     */    
    private void setDac(int aNum, Double aVolts)
    {
        long lCounts;

        if (aVolts> VOLT_RANGE_STG)
        {
            aVolts = VOLT_RANGE_STG;
        }

        if (aVolts < -VOLT_RANGE_STG)
        {
            aVolts = -VOLT_RANGE_STG;
        }

        if ((aNum >= 0 ) && (aNum < mWAxesInSys))
        {
            // convert value from volts to a value between -4095 and 4096
            lCounts = (long) (DAC_RANGE_STG * (aVolts/VOLT_RANGE_STG));
            rawDAC((short)aNum, lCounts);
        }
    }

    /**
     * Read the value of encoder iNum. Returns -1 if the encoder doesn't exist,
     * returns 1 if read was OK
     * @param aNum - number of encoder to be read.
     * @param aValue - pointer to value read.
     */    
    private int getEncoder(int aNum, Long aValue)
    {
        LONGBYTE [] lbEnc = new LONGBYTE[MAX_AXIS];

        encReadAll(lbEnc);

        if ((aNum >= 0) || (aNum < mWAxesInSys))
        {
            aValue = lbEnc[aNum].getLong() - mHomeposition[aNum];
            return aNum;
        }
        else
        {
            return -1;
        }
    }
    
    /**
     * Checks if board is present and what is its model number
     * @param aBaseAddress - The address of the board in I/O space
     */
    private short brdtstOK(short aBaseAddress)
    {
        short BrdtstAddress;
        short SerSeq, HighNibble;

        BrdtstAddress = (short)(aBaseAddress + BRDTST);

        SerSeq = 0;
        for (int j = 7; j >= 0; j--)
        {
            HighNibble = (short)(fInP(BrdtstAddress) >> 4);
            if ((HighNibble & 8) != 0)     // is SER set
            {
               // shift bit to position specifed by Q2, Q1, Q0
               // which are the lower three bits.  Put bit in SerSeq.
               SerSeq |= 1 << (HighNibble & 7);
            }
        }
        if (SerSeq == 0x75)        // SER sequence is 01110101
        {
            mWModel = MODEL1;
            return (1);
        }
        else if (SerSeq == 0x74)   // SER sequence is 01110100
        {
            mWModel = MODEL2;
            return (1);
        }
        else
        {
            mWModel = MODEL_NO_ID;
            return (0);
        }
    }
    
    /**
     * Initializes various aspects of the board
     * @param aWAdd - Address of the board in I/O space.
     */
    private void stg_Init(short aWAdd)
    {
        mByIndexPollAxis = 0;
        mByIndexPulsePolarity = 1;

        mWBaseAddress = aWAdd;
        if ( brdtstOK(aWAdd) != 0 )    // determines model and fills in wModel
        {
            mWBaseAddress = 0;
            mWNoBoardFlag = NO_BOARD;
            mSystemReady = false;
            mSystemAvailable = false;
            return;
        }

        mWNoBoardFlag = BOARD_PRESENT;

        if (mWModel == MODEL2)
        {
            fOutP((short)(mWBaseAddress + CNTRL1), (char)CNTRL1_NOT_SLAVE);
        }

        // stop interrupts: we're not using them
        fOutP((short)(mWBaseAddress + MIO_2),(char) 0x8b);  // initialize CNTRL0 as output reg.

        fOutP((short)(mWBaseAddress + CNTRL1),
              (char)((fInP((short)(mWBaseAddress + CNTRL1)) & CNTRL1_NOT_SLAVE) | 0xf0));
        encoderInit();
    }
    
    /**
     * Initializes the encoder chips of the board.
     */
    private void encoderInit()
    {
        // It is possible that the encoder counts are being held by battery
        // backup, so we'll read the encoders, and save the values
        // Then we'll initialize the encoder chips, since it's more likely that
        // the encoders were not kept alive by battery and need to be initialized

        LONGBYTE [] enc = new LONGBYTE[8];
        encReadAll(enc);

        for (short wAdd = (short)(mWBaseAddress + CNT0_C);
                                     wAdd <= mWBaseAddress + CNT6_C; wAdd +=4)
        {
            // we're going to be slick and do two chips at a time, that's why
            // the registers are arranged data, data, control, control.  You
            // can do two at a time, by using word operations, instead of
            // byte operations.  Not a big deal for initializing, but reading is
            // done pretty often.

            fOutPW(wAdd, (short)0x2020);   // master reset

            // Set Counter Command Register - Input Control, OL Load (P3),
            // and Enable Inputs A and B (INA/B).

            fOutPW(wAdd, (short)0x6868);

            // Set Counter Command Register - Output Control

            fOutPW(wAdd, (short)0x8080);

            // Set Counter Command Register - Quadrature

            fOutPW(wAdd,(short) 0xc3c3);

            fOutPW(wAdd, (short)0x0404);  //reset counter to zero
        }

        //  Figure out how many axes are on the card

        for (short wA = (short)(mWBaseAddress + CNT0_D); wA <= mWBaseAddress + CNT6_D; wA +=4)
        {
            final short wTestPat = 0x5aa5;

            // reset address pointer

            fOutPW((short)(wA + 2), (short)0x0101);

            // write a pattern to the preset register

            fOutPW(wA, wTestPat);
            fOutPW(wA, wTestPat);
            fOutPW(wA, wTestPat);

            // transfer the preset register to the count register

            fOutPW((short)(wA + 2), (short)0x0909);

            // transfer counter to output latch

            fOutPW((short)(wA + 2), (short)0x0202);

            // read the output latch and see if it matches

            if (fInPW(wA) != wTestPat)
                break;
            if (fInPW(wA) != wTestPat)
                break;
            if (fInPW(wA) != wTestPat)
                break;

            // now replace the values that you saved previously, in case the
            // encoder was battery backed up

            fOutP(wA, (char)enc[mWAxesInSys].getByte()[0]);
            fOutP(wA, (char)enc[mWAxesInSys].getByte()[1]);
            fOutP(wA, (char)enc[mWAxesInSys].getByte()[2]);
            
            fOutP((short)(wA + 1), (char)enc[mWAxesInSys + 1].getByte()[0]);
            fOutP((short)(wA + 1), (char)enc[mWAxesInSys + 1].getByte()[1]);
            fOutP((short)(wA + 1), (char)enc[mWAxesInSys + 1].getByte()[2]);

            // transfer the preset register to the count register

            fOutPW((short)(wA + 2), (short)0x0909);

            // transfer counter to output latch  debug

            fOutPW((short)(wA + 2), (short)0x0202);  // debug

            mWAxesInSys += 2;
        }
    }
    
    /**
     * Latches all encoders.
     */
    private void encoderLatch()
    {
        // normally you'll have the timer latch the data in hardware, but */
        // if the timer isn't running, we need to latch it ourselves. */

        // BUG FIX-- don't go past 4 axes on 4 axis board
        fOutPW((short)(mWBaseAddress + CNT0_C), (short)0x0303);
        fOutPW((short)(mWBaseAddress + CNT2_C), (short)0x0303);
        if (mWAxesInSys > 4)
        {
            fOutPW((short)(mWBaseAddress + CNT4_C), (short)0x0303);
            fOutPW((short)(mWBaseAddress + CNT6_C), (short)0x0303);
        }
    }
    
    /**
     * Finds the base address for the board.
     */    
    private short findBaseAddress()
    {
       short i;
       short ioAdd;

       // search for all possible addresses
       for (i = 15; i >= 0; i--)
       {
           ioAdd = (short)(i * 0x20 + 0x200);
           if ( brdtstOK(ioAdd) != 0 )
               return (ioAdd);
       }
       return(0);
    }

    private byte [] byOldByte2 = new byte[MAX_AXIS];
    private byte [] byEncHighByte = new byte[MAX_AXIS];
    
    /**
     * Latches and reads all encoders at once.
     * @param aLbEnc - array of the encoder values
     */    
    private void encReadAll( LONGBYTE [] aLbEnc)
    {
        WORDBYTE wbTransfer = new WORDBYTE();
        byte [] bRead = new byte[2];
        short i;

        // Disable interrupts here?  No, the timer will latch new data in the
        // hardware anyway.  Maybe we should stop the timer?  In an interrupt
        // service routine, you're synchronized with the timer; so the readings
        // will never change while you're reading them.  If you're polling, you
        // would first latch the encoder counts with the EncoderLatch() function.
        // But, the timer could latch the counts again, in the middle of the read.
        // A critical section will help in some extreme cases.

        // reset counter internal addr ptr to point to first byte

        encoderLatch();

        for (int add = mWBaseAddress + CNT0_C; add <= mWBaseAddress + CNT6_C; add +=4)
            fOutPW((short)add, (short)0x0101);

        for (i = 0; i < 3; i++)            // 24 bits means get 3 bytes each
        {
            wbTransfer.setWord((short)fInPW((short)(mWBaseAddress + CNT0_D)));
            bRead[HIGH_BYTE] = wbTransfer.getByte()[HIGH_BYTE];
            bRead[LOW_BYTE] = wbTransfer.getByte()[LOW_BYTE];
            aLbEnc[0].setByte(bRead);
            aLbEnc[1].setByte(bRead);

            wbTransfer.setWord((short)fInPW((short)(mWBaseAddress + CNT2_D)));
            bRead[HIGH_BYTE] = wbTransfer.getByte()[HIGH_BYTE];
            bRead[LOW_BYTE] = wbTransfer.getByte()[LOW_BYTE];
            aLbEnc[2].setByte(bRead);
            aLbEnc[3].setByte(bRead);

            wbTransfer.setWord((short)fInPW((short)(mWBaseAddress + CNT4_D)));
            bRead[HIGH_BYTE] = wbTransfer.getByte()[HIGH_BYTE];
            bRead[LOW_BYTE] = wbTransfer.getByte()[LOW_BYTE];
            aLbEnc[4].setByte(bRead);
            aLbEnc[5].setByte(bRead);

            wbTransfer.setWord((short)fInPW((short)(mWBaseAddress + CNT6_D)));
            bRead[HIGH_BYTE] = wbTransfer.getByte()[HIGH_BYTE];
            bRead[LOW_BYTE] = wbTransfer.getByte()[LOW_BYTE];
            aLbEnc[6].setByte(bRead);
            aLbEnc[7].setByte(bRead);
        }

        // maintain the high byte, to extend the counter to 32 bits
        //
        // base decisions to increment or decrement the high byte
        // on the highest 2 bits of the 24 bit value.  To get the
        // highest 2 bits, use 0xc0 as a mask on byte [2] (the third
        // byte).

        for (i = 0; i < MAX_AXIS; i++)
        {
            // check for -1 to 0 transition

            if (    ( (byOldByte2[i]    & 0xc0) == 0xc0 ) // 11xxxxxx
                 && ( (aLbEnc[i].getByte()[2] & 0xc0) == 0 )    // 00xxxxxx
               )
               byEncHighByte[i]++;

            // check for 0 to -1 transition

            if (    ( (byOldByte2[i]    & 0xc0) == 0 )    // 00xxxxxx
                 && ( (aLbEnc[i].getByte()[2] & 0xc0) == 0xc0 ) // 11xxxxxx
               )
               byEncHighByte[i]--;

            byte [] updateByte = aLbEnc[i].getByte();
            updateByte[3] = byEncHighByte[i];
            aLbEnc[i].setByte(updateByte);

            byOldByte2[i] = aLbEnc[i].getByte()[2];    // current byte 2 becomes old one

        }
    }
    
    /**
     * Writes to DAC nAxis value lCounts
     * @param nAxis - value of DAC to which to write
     * @param lCounts - value to be written to the DAC
     */    
    private void rawDAC(short nAxis, long lCounts)
    {
        //--------------------------------------------------------------------
        // input / output:
        //
        //    lCounts (decimal) ... -lCounts ... +0x1000 ... volts
        //
        //     0x1000  (4096)     0xfffff000           0       +10
        //          0                      0      0x1000         0
        // 0xfffff001 (-4095)          0xfff      0x1fff       -10
        //--------------------------------------------------------------------
        // So, the domain might be different than you expected. I expected:
        //     0xf000 (-4096)  to  0xfff (4095), rather than
        //     0xf001 (-4095)  to 0x1000 (4096)
        //--------------------------------------------------------------------

        // reverse slope so positive counts give positive voltage
        lCounts = - lCounts;

        // shift for DAC
        lCounts += 0x1000;

        if (lCounts > 0x1FFF)    // clamp + output
        {
            lCounts = 0x1FFF;
        }
        if (lCounts < 0)         // clamp - output
        {
            lCounts = 0;
        }

        fOutPW((short)(mWBaseAddress + DAC_0 + (nAxis << 1)), (short)lCounts);
    }
    
    /**
     * Returns board base address.
     */
    private int getBaseAddress()
    {
        return (mWBaseAddress);
    }
    
    /**
     * Open board.
     * @return - Return 0 is operation succeeds, -1 if an error occurs.
     */    
    @Override
    public int open()
    {
        // find base address of board
        mWBaseAddress = findBaseAddress();
        stg_Init(mWBaseAddress);

        if (mWNoBoardFlag == NO_BOARD)
        {
            mSystemReady = false;
            mSystemAvailable = false;
            return (-1);
        }

        // initialize motors to zero.
        for (int i = 0; i<mWAxesInSys; i++)
        {
            Long appo = new Long(0);
            mHomeposition[i] = 0;
            getEncoder(i, appo);
            mHomeposition[i] = appo;
        }

        mSystemReady = true;
        mSystemAvailable = true;
        return (0);
    }
    
    /**
     * Sets all DACs to zero
     * @return - Return 0 is operation succeeds, -1 if an error occurs.
     */    
    @Override
    public int close()
    {
        // set all the DAC outputs to 0.
        for (int i = 0; i<mWAxesInSys; i++)
        {
            setDac(i,new Double(0));
        }

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
     * Send a command to the Servo2Go board. Possible commands are:
     * CHAI_CMD_GET_DEVICE_STATE: returns an int (1 board is ready, 0 board is NOT ready).
     * CHAI_CMD_GET_ENCODER_0: reads encoder 0, returns counts value in a long.
     * CHAI_CMD_GET_ENCODER_1: reads encoder 1, returns counts value in a long.
     * CHAI_CMD_GET_ENCODER_2: reads encoder 2, returns counts value in a long.
     * CHAI_CMD_GET_ENCODER_3: reads encoder 3, returns counts value in a long.
     * CHAI_CMD_GET_ENCODER_4: reads encoder 4, returns counts value in a long, and a value of 0 if the encoder does not exist.
     * CHAI_CMD_GET_ENCODER_5: reads encoder 5, returns counts value in a long, and a value of 0 if the encoder does not exist.
     * CHAI_CMD_GET_ENCODER_6: reads encoder 6, returns counts value in a long, and a value of 0 if the encoder does not exist.
     * CHAI_CMD_GET_ENCODER_7: reads encoder 7, returns counts value in a long, and a value of 0 if the encoder does not exist.
     * 
     * CHAI_CMD_SET_DAC_0: writes a voltage to DAC 0 a value between +10 and -10 volts, which is a double.
     * CHAI_CMD_SET_DAC_1: writes a voltage to DAC 1 a value between +10 and -10 volts, which is a double.
     * CHAI_CMD_SET_DAC_2: writes a voltage to DAC 2 a value between +10 and -10 volts, which is a double.
     * CHAI_CMD_SET_DAC_3: writes a voltage to DAC 3 a value between +10 and -10 volts, which is a double.
     * CHAI_CMD_SET_DAC_4: writes a voltage to DAC 4 a value between +10 and -10 volts, which is a double. If this axis is not supported no action is taken.
     * CHAI_CMD_SET_DAC_5: writes a voltage to DAC 5 a value between +10 and -10 volts, which is a double. If this axis is not supported no action is taken.
     * CHAI_CMD_SET_DAC_6: writes a voltage to DAC 6 a value between +10 and -10 volts, which is a double. If this axis is not supported no action is taken.
     * CHAI_CMD_SET_DAC_7: writes a voltage to DAC 7 a value between +10 and -10 volts, which is a double. If this axis is not supported no action is taken.
     * @param aCommand - Selected command.
     * @param aData - Pointer to the corresponding data structure.
     * @return - Return status of command.
     */    
    @Override
    public int command(int aCommand, Object aData) {
        int retval = CHAI_MSG_OK;
        if (mSystemReady) {
            switch (aCommand) {
                case CHAI_CMD_GET_DEVICE_STATE: {
                    if (mSystemReady) {
                        aData = 0;
                    } else {
                        aData = -1;
                    }
                }
                break;
                // read encoder 0
                case CHAI_CMD_GET_ENCODER_0: {
                    Long iValue = Long.parseLong(aData.toString());
                    getEncoder(0, iValue);
                    aData = iValue.longValue();
                }
                break;
                // read encoder 1
                case CHAI_CMD_GET_ENCODER_1: {
                    Long iValue = Long.parseLong(aData.toString());
                    getEncoder(1, iValue);
                    aData = iValue.longValue();
                }
                break;
                // read encoder 2
                case CHAI_CMD_GET_ENCODER_2: {
                    Long iValue = Long.parseLong(aData.toString());
                    getEncoder(2, iValue);
                    aData = iValue.longValue();
                }
                break;
                // read encoder 3
                case CHAI_CMD_GET_ENCODER_3: {
                    Long iValue = Long.parseLong(aData.toString());
                    getEncoder(3, iValue);
                    aData = iValue.longValue();
                }
                break;
                // read encoder 4
                case CHAI_CMD_GET_ENCODER_4: {
                    Long iValue = Long.parseLong(aData.toString());
                    if (getEncoder(4, iValue) < 0) {
                        iValue = (long) 0;
                    }
                    aData = iValue.longValue();
                }
                break;
                // read encoder 5
                case CHAI_CMD_GET_ENCODER_5: {
                    Long iValue = Long.parseLong(aData.toString());
                    if (getEncoder(5, iValue) < 0) {
                        iValue = (long) 0;
                    }
                    aData = iValue.longValue();
                }
                break;
                // read encoder 6
                case CHAI_CMD_GET_ENCODER_6: {
                    // check what model of board this is...
                    Long iValue = Long.parseLong(aData.toString());
                    if (getEncoder(6, iValue) < 0) {
                        iValue = (long) 0;
                    }
                    aData = iValue.longValue();
                }
                break;
                // read encoder 7
                case CHAI_CMD_GET_ENCODER_7: {
                    Long iValue = Long.parseLong(aData.toString());
                    if (getEncoder(7, iValue) < 0) {
                        iValue = (long) 0;
                    }
                    aData = iValue.longValue();
                }
                break;

                // write motor 0
                case CHAI_CMD_SET_DAC_0: {
                    Double iVolts = Double.parseDouble(aData.toString());
                    setDac(0, iVolts);
                    aData = iVolts.doubleValue();
                }
                break;
                // write motor 1
                case CHAI_CMD_SET_DAC_1: {
                    Double iVolts = Double.parseDouble(aData.toString());
                    setDac(1, iVolts);
                    aData = iVolts.doubleValue();
                }
                break;
                // write motor 2
                case CHAI_CMD_SET_DAC_2: {
                    Double iVolts = Double.parseDouble(aData.toString());
                    setDac(2, iVolts);
                    aData = iVolts.doubleValue();
                }
                break;
                // write motor 3
                case CHAI_CMD_SET_DAC_3: {
                    Double iVolts = Double.parseDouble(aData.toString());
                    setDac(3, iVolts);
                    aData = iVolts.doubleValue();
                }
                break;
                // write motor 4
                case CHAI_CMD_SET_DAC_4: {
                    Double iVolts = Double.parseDouble(aData.toString());
                    setDac(4, iVolts);
                    aData = iVolts.doubleValue();
                }
                break;
                // write motor 5
                case CHAI_CMD_SET_DAC_5: {
                    Double iVolts = Double.parseDouble(aData.toString());
                    setDac(5, iVolts);
                    aData = iVolts.doubleValue();
                }
                break;
                // write motor 6
                case CHAI_CMD_SET_DAC_6: {
                    Double iVolts = Double.parseDouble(aData.toString());
                    setDac(6, iVolts);
                    aData = iVolts.doubleValue();
                }
                break;
                // write motor 7
                case CHAI_CMD_SET_DAC_7: {
                    Double iVolts = Double.parseDouble(aData.toString());
                    setDac(7, iVolts);
                    aData = iVolts.doubleValue();
                }
                break;
                // function is not implemented
                default:
                    retval = CHAI_MSG_NOT_IMPLEMENTED;
            }
        } else {
            retval = CHAI_MSG_ERROR;
        }

        return (retval);
    }
    
    //---------------------------------------------------------------------------
    // Input Output redefinitions
    //---------------------------------------------------------------------------
    
    public static void fOutP(short port, char data)
    {
       dlPort.DlPortWritePortUchar(port,data);
    }

    public static void fOutPW(short port, short data)
    {
       dlPort.DlPortWritePortUshort(port, data);
    }

    public static char fInP(short port)
    {
       return (dlPort.DlPortReadPortUchar(port));
    }

    public static short fInPW(short port)
    {
       return (dlPort.DlPortReadPortUshort(port));
    }

    interface DlPortAPI
    {
        char DlPortReadPortUchar(
            long Port
            );

        short DlPortReadPortUshort(
            long Port
            );

        long DlPortReadPortUlong(
            long Port
            );

        void DlPortReadPortBufferUchar(
            long Port,
            String Buffer,
            long  Count
            );

        void DlPortReadPortBufferUshort(
            long Port,
            String Buffer,
            long Count
            );

        void DlPortReadPortBufferUlong(
            long Port,
            LongByReference Buffer,
            long Count
            );

        void DlPortWritePortUchar(
            long Port,
            char Value
            );

        void DlPortWritePortUshort(
            long Port,
            short Value
            );

        void DlPortWritePortUlong(
            long Port,
            long Value
            );

        void DlPortWritePortBufferUchar(
            long Port,
            String Buffer,
            long  Count
            );

        void DlPortWritePortBufferUshort(
            long Port,
            ShortByReference Buffer,
            long Count
            );

        void DlPortWritePortBufferUlong(
            long Port,
            LongByReference Buffer,
            long Count
            );

    }

    /**
     * Internal information to ServoToGo
     */ 
    class LONGBYTE extends Union
    {
        public long mLong;
        public byte [] mByte = new byte[4];

        public void setLong(long aLong)
        {
            this.writeField("mLong",aLong);
        }
        
        public void setByte(byte [] aByte)
        {
            this.writeField("mByte",aByte);
        }

        public long getLong()
        {
            return (Long)this.readField("mLong");
        }

        public byte [] getByte()
        {
            return (byte [])this.readField("mByte");
        }
    }

    private static final int HIGH_BYTE = 0;
    private static final int LOW_BYTE = 1;

    /**
     * Internal information to ServoToGo
     */ 
    class WORDBYTE extends Union
    {
        public short mWord;
        public byte [] mByte = new byte[2];

        public void setWord(short aWord)
        {
            this.writeField("mWord",aWord);
        }

        public void setByte(byte [] aByte)
        {
            this.writeField("mByte",aByte);
        }

        public short getWord()
        {
            return (Short)this.readField("mWord");
        }

        public byte [] getByte()
        {
            return (byte [])this.readField("mByte");
        }

    }

}
