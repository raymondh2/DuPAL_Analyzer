package info.hkzlab.dupal.analyzer.palanalisys.explorers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.hkzlab.dupal.analyzer.board.boardio.DuPALCmdInterface;
import info.hkzlab.dupal.analyzer.devices.PALSpecs;
import info.hkzlab.dupal.analyzer.exceptions.DuPALBoardException;
import info.hkzlab.dupal.analyzer.palanalisys.graph.OutState;
import info.hkzlab.dupal.analyzer.palanalisys.graph.OutStatePins;
import info.hkzlab.dupal.analyzer.utilities.BitUtils;

public class OSExplorer {
    private static final Logger logger = LoggerFactory.getLogger(OSExplorer.class);

    private OSExplorer() {};

    public static void exploreOutStates(final DuPALCmdInterface dpci, final int ioAsOutMask) throws DuPALBoardException {
        PALSpecs pSpecs = dpci.palSpecs;
        int maxLinks = 1 << (pSpecs.getPinCount_IN() + (pSpecs.getPinCount_IO()-BitUtils.countBits(ioAsOutMask)));
        OutState curState;

        curState = generateOutStateForIdx(dpci, 0, ioAsOutMask, maxLinks);
        logger.info("exploreOutStates() -> Initial state: " + curState);

        //while(curState != null) {

        //}
    }

    private static OutState generateOutStateForIdx(final DuPALCmdInterface dpci, final int idx, final int ioAsOutMask, int maxLinks)
            throws DuPALBoardException {
        PALSpecs pSpecs = dpci.palSpecs;
        int ioAsOut_W = BitUtils.scatterBitField(BitUtils.consolidateBitField(ioAsOutMask, pSpecs.getMask_IO_R()), pSpecs.getMask_IO_W());
        int pinState_A, pinState_B;

        int w_idx = BitUtils.scatterBitField(idx, dpci.palSpecs.getMask_IN());
        w_idx |= BitUtils.scatterBitField((idx >> dpci.palSpecs.getPinCount_IN()), dpci.palSpecs.getMask_IO_W() & ~ioAsOut_W);

        dpci.write(w_idx);
        pinState_A = dpci.read();
        dpci.write(w_idx | pSpecs.getMask_O_W() | ioAsOut_W);
        pinState_B = dpci.read();

        OutStatePins osp = extractOutPinStates(pSpecs, ioAsOutMask, pinState_A, pinState_B);

        return new OutState(osp, maxLinks);
    }

    private static OutStatePins extractOutPinStates(PALSpecs pSpecs, int ioAsOutMask, int read_a, int read_b) {
        int out, hiz;

        hiz = (read_a ^ read_b) & (ioAsOutMask | pSpecs.getMask_O_R());
        out = (read_a & (ioAsOutMask | pSpecs.getMask_O_R())) & ~hiz;

        return new OutStatePins(out, hiz);
    }
}
