package com.exosphere.tests;

import com.exosphere.game.Assets;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * exosphere - AssetsTest
 * Created by tistatos on 9/7/15.
 */
public class AssetsTest {

    @Test
    public void testLoad() throws Exception {
        assertTrue(Assets.load());
    }
}