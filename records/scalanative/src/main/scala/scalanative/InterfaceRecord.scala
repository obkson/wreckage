package scalanative

case class InterfaceRecord1(
   f1: Int
) extends Record
  with    F1[Int]

case class InterfaceRecord2(
   f1: Int,  f2: Int
) extends Record
  with  F1[Int] with  F2[Int]

case class InterfaceRecord4(
   f1: Int,  f2: Int,  f3: Int,  f4: Int
) extends Record
  with  F1[Int] with  F2[Int] with  F3[Int] with  F4[Int]

case class InterfaceRecord5(
   f1: Int,  f2: Int,  f3: Int,  f4: Int,  f5: Int
) extends Record
  with  F1[Int] with  F2[Int] with  F3[Int] with  F4[Int] with  F5[Int]

case class InterfaceRecord8(
   f1: Int,  f2: Int,  f3: Int,  f4: Int,  f5: Int,  f6: Int,  f7: Int,  f8: Int
) extends Record
  with  F1[Int] with  F2[Int] with  F3[Int] with  F4[Int] with  F5[Int] with  F6[Int] with  F7[Int] with  F8[Int]

case class InterfaceRecord10(
   f1: Int,  f2: Int,  f3: Int,  f4: Int,  f5: Int,  f6: Int,  f7: Int,  f8: Int,  f9: Int, f10: Int
) extends Record
  with  F1[Int] with  F2[Int] with  F3[Int] with  F4[Int] with  F5[Int] with  F6[Int] with  F7[Int] with  F8[Int] with  F9[Int] with  F10[Int]

case class InterfaceRecord15(
   f1: Int,  f2: Int,  f3: Int,  f4: Int,  f5: Int,  f6: Int,  f7: Int,  f8: Int,  f9: Int, f10: Int,
  f11: Int, f12: Int, f13: Int, f14: Int, f15: Int
) extends Record
  with  F1[Int] with  F2[Int] with  F3[Int] with  F4[Int] with  F5[Int] with  F6[Int] with  F7[Int] with  F8[Int] with  F9[Int] with F10[Int]
  with F11[Int] with F12[Int] with F13[Int] with F14[Int] with F15[Int]

case class InterfaceRecord16(
   f1: Int,  f2: Int,  f3: Int,  f4: Int,  f5: Int,  f6: Int,  f7: Int,  f8: Int,  f9: Int, f10: Int,
  f11: Int, f12: Int, f13: Int, f14: Int, f15: Int, f16: Int
) extends Record
  with  F1[Int] with  F2[Int] with  F3[Int] with  F4[Int] with  F5[Int] with  F6[Int] with  F7[Int] with  F8[Int] with  F9[Int] with F10[Int]
  with F11[Int] with F12[Int] with F13[Int] with F14[Int] with F15[Int] with F16[Int]

case class InterfaceRecord20(
   f1: Int,  f2: Int,  f3: Int,  f4: Int,  f5: Int,  f6: Int,  f7: Int,  f8: Int,  f9: Int, f10: Int,
  f11: Int, f12: Int, f13: Int, f14: Int, f15: Int, f16: Int, f17: Int, f18: Int, f19: Int, f20: Int
) extends Record
  with  F1[Int] with  F2[Int] with  F3[Int] with  F4[Int] with  F5[Int] with  F6[Int] with  F7[Int] with  F8[Int] with  F9[Int] with F10[Int]
  with F11[Int] with F12[Int] with F13[Int] with F14[Int] with F15[Int] with F16[Int] with F17[Int] with F18[Int] with F19[Int] with F20[Int]

case class InterfaceRecord25(
   f1: Int,  f2: Int,  f3: Int,  f4: Int,  f5: Int,  f6: Int,  f7: Int,  f8: Int,  f9: Int, f10: Int,
  f11: Int, f12: Int, f13: Int, f14: Int, f15: Int, f16: Int, f17: Int, f18: Int, f19: Int, f20: Int,
  f21: Int, f22: Int, f23: Int, f24: Int, f25: Int
) extends Record
  with  F1[Int] with  F2[Int] with  F3[Int] with  F4[Int] with  F5[Int] with  F6[Int] with  F7[Int] with  F8[Int] with  F9[Int] with F10[Int]
  with F11[Int] with F12[Int] with F13[Int] with F14[Int] with F15[Int] with F16[Int] with F17[Int] with F18[Int] with F19[Int] with F20[Int]
  with F21[Int] with F22[Int] with F23[Int] with F24[Int] with F25[Int]

case class InterfaceRecord30(
   f1: Int,  f2: Int,  f3: Int,  f4: Int,  f5: Int,  f6: Int,  f7: Int,  f8: Int,  f9: Int, f10: Int,
  f11: Int, f12: Int, f13: Int, f14: Int, f15: Int, f16: Int, f17: Int, f18: Int, f19: Int, f20: Int,
  f21: Int, f22: Int, f23: Int, f24: Int, f25: Int, f26: Int, f27: Int, f28: Int, f29: Int, f30: Int
) extends Record
  with  F1[Int] with  F2[Int] with  F3[Int] with  F4[Int] with  F5[Int] with  F6[Int] with  F7[Int] with  F8[Int] with  F9[Int] with F10[Int]
  with F11[Int] with F12[Int] with F13[Int] with F14[Int] with F15[Int] with F16[Int] with F17[Int] with F18[Int] with F19[Int] with F20[Int]
  with F21[Int] with F22[Int] with F23[Int] with F24[Int] with F25[Int] with F26[Int] with F27[Int] with F28[Int] with F29[Int] with F30[Int]

case class InterfaceRecord32(
   f1: Int,  f2: Int,  f3: Int,  f4: Int,  f5: Int,  f6: Int,  f7: Int,  f8: Int,  f9: Int, f10: Int,
  f11: Int, f12: Int, f13: Int, f14: Int, f15: Int, f16: Int, f17: Int, f18: Int, f19: Int, f20: Int,
  f21: Int, f22: Int, f23: Int, f24: Int, f25: Int, f26: Int, f27: Int, f28: Int, f29: Int, f30: Int,
  f31: Int, f32: Int
) extends Record
  with  F1[Int] with  F2[Int] with  F3[Int] with  F4[Int] with  F5[Int] with  F6[Int] with  F7[Int] with  F8[Int] with  F9[Int] with F10[Int]
  with F11[Int] with F12[Int] with F13[Int] with F14[Int] with F15[Int] with F16[Int] with F17[Int] with F18[Int] with F19[Int] with F20[Int]
  with F21[Int] with F22[Int] with F23[Int] with F24[Int] with F25[Int] with F26[Int] with F27[Int] with F28[Int] with F29[Int] with F30[Int]
  with F31[Int] with F32[Int]

case class InterfaceRecord50(
   f1: Int,  f2: Int,  f3: Int,  f4: Int,  f5: Int,  f6: Int,  f7: Int,  f8: Int,  f9: Int, f10: Int,
  f11: Int, f12: Int, f13: Int, f14: Int, f15: Int, f16: Int, f17: Int, f18: Int, f19: Int, f20: Int,
  f21: Int, f22: Int, f23: Int, f24: Int, f25: Int, f26: Int, f27: Int, f28: Int, f29: Int, f30: Int,
  f31: Int, f32: Int, f33: Int, f34: Int, f35: Int, f36: Int, f37: Int, f38: Int, f39: Int, f40: Int,
  f41: Int, f42: Int, f43: Int, f44: Int, f45: Int, f46: Int, f47: Int, f48: Int, f49: Int, f50: Int
) extends Record
  with  F1[Int] with  F2[Int] with  F3[Int] with  F4[Int] with  F5[Int] with  F6[Int] with  F7[Int] with  F8[Int] with  F9[Int] with F10[Int]
  with F11[Int] with F12[Int] with F13[Int] with F14[Int] with F15[Int] with F16[Int] with F17[Int] with F18[Int] with F19[Int] with F20[Int]
  with F21[Int] with F22[Int] with F23[Int] with F24[Int] with F25[Int] with F26[Int] with F27[Int] with F28[Int] with F29[Int] with F30[Int]
  with F31[Int] with F32[Int] with F33[Int] with F34[Int] with F35[Int] with F36[Int] with F37[Int] with F38[Int] with F39[Int] with F40[Int]
  with F41[Int] with F42[Int] with F43[Int] with F44[Int] with F45[Int] with F46[Int] with F47[Int] with F48[Int] with F49[Int] with F50[Int]

case class InterfaceRecord64(
   f1: Int,  f2: Int,  f3: Int,  f4: Int,  f5: Int,  f6: Int,  f7: Int,  f8: Int,  f9: Int, f10: Int,
  f11: Int, f12: Int, f13: Int, f14: Int, f15: Int, f16: Int, f17: Int, f18: Int, f19: Int, f20: Int,
  f21: Int, f22: Int, f23: Int, f24: Int, f25: Int, f26: Int, f27: Int, f28: Int, f29: Int, f30: Int,
  f31: Int, f32: Int, f33: Int, f34: Int, f35: Int, f36: Int, f37: Int, f38: Int, f39: Int, f40: Int,
  f41: Int, f42: Int, f43: Int, f44: Int, f45: Int, f46: Int, f47: Int, f48: Int, f49: Int, f50: Int,
  f51: Int, f52: Int, f53: Int, f54: Int, f55: Int, f56: Int, f57: Int, f58: Int, f59: Int, f60: Int,
  f61: Int, f62: Int, f63: Int, f64: Int
) extends Record
  with  F1[Int] with  F2[Int] with  F3[Int] with  F4[Int] with  F5[Int] with  F6[Int] with  F7[Int] with  F8[Int] with  F9[Int] with F10[Int]
  with F11[Int] with F12[Int] with F13[Int] with F14[Int] with F15[Int] with F16[Int] with F17[Int] with F18[Int] with F19[Int] with F20[Int]
  with F21[Int] with F22[Int] with F23[Int] with F24[Int] with F25[Int] with F26[Int] with F27[Int] with F28[Int] with F29[Int] with F30[Int]
  with F31[Int] with F32[Int] with F33[Int] with F34[Int] with F35[Int] with F36[Int] with F37[Int] with F38[Int] with F39[Int] with F40[Int]
  with F41[Int] with F42[Int] with F43[Int] with F44[Int] with F45[Int] with F46[Int] with F47[Int] with F48[Int] with F49[Int] with F50[Int]
  with F51[Int] with F52[Int] with F53[Int] with F54[Int] with F55[Int] with F56[Int] with F57[Int] with F58[Int] with F59[Int] with F60[Int]
  with F61[Int] with F62[Int] with F63[Int] with F64[Int]

case class InterfaceRecord100(
   f1: Int,  f2: Int,  f3: Int,  f4: Int,  f5: Int,  f6: Int,  f7: Int,  f8: Int,  f9: Int, f10: Int,
  f11: Int, f12: Int, f13: Int, f14: Int, f15: Int, f16: Int, f17: Int, f18: Int, f19: Int, f20: Int,
  f21: Int, f22: Int, f23: Int, f24: Int, f25: Int, f26: Int, f27: Int, f28: Int, f29: Int, f30: Int,
  f31: Int, f32: Int, f33: Int, f34: Int, f35: Int, f36: Int, f37: Int, f38: Int, f39: Int, f40: Int,
  f41: Int, f42: Int, f43: Int, f44: Int, f45: Int, f46: Int, f47: Int, f48: Int, f49: Int, f50: Int,
  f51: Int, f52: Int, f53: Int, f54: Int, f55: Int, f56: Int, f57: Int, f58: Int, f59: Int, f60: Int,
  f61: Int, f62: Int, f63: Int, f64: Int, f65: Int, f66: Int, f67: Int, f68: Int, f69: Int, f70: Int,
  f71: Int, f72: Int, f73: Int, f74: Int, f75: Int, f76: Int, f77: Int, f78: Int, f79: Int, f80: Int,
  f81: Int, f82: Int, f83: Int, f84: Int, f85: Int, f86: Int, f87: Int, f88: Int, f89: Int, f90: Int,
  f91: Int, f92: Int, f93: Int, f94: Int, f95: Int, f96: Int, f97: Int, f98: Int, f99: Int, f100: Int
) extends Record
  with  F1[Int] with  F2[Int] with  F3[Int] with  F4[Int] with  F5[Int] with  F6[Int] with  F7[Int] with  F8[Int] with  F9[Int] with F10[Int]
  with F11[Int] with F12[Int] with F13[Int] with F14[Int] with F15[Int] with F16[Int] with F17[Int] with F18[Int] with F19[Int] with F20[Int]
  with F21[Int] with F22[Int] with F23[Int] with F24[Int] with F25[Int] with F26[Int] with F27[Int] with F28[Int] with F29[Int] with F30[Int]
  with F31[Int] with F32[Int] with F33[Int] with F34[Int] with F35[Int] with F36[Int] with F37[Int] with F38[Int] with F39[Int] with F40[Int]
  with F41[Int] with F42[Int] with F43[Int] with F44[Int] with F45[Int] with F46[Int] with F47[Int] with F48[Int] with F49[Int] with F50[Int]
  with F51[Int] with F52[Int] with F53[Int] with F54[Int] with F55[Int] with F56[Int] with F57[Int] with F58[Int] with F59[Int] with F60[Int]
  with F61[Int] with F62[Int] with F63[Int] with F64[Int] with F65[Int] with F66[Int] with F67[Int] with F68[Int] with F69[Int] with F70[Int]
  with F71[Int] with F72[Int] with F73[Int] with F74[Int] with F75[Int] with F76[Int] with F77[Int] with F78[Int] with F79[Int] with F80[Int]
  with F81[Int] with F82[Int] with F83[Int] with F84[Int] with F85[Int] with F86[Int] with F87[Int] with F88[Int] with F89[Int] with F90[Int]
  with F91[Int] with F92[Int] with F93[Int] with F94[Int] with F95[Int] with F96[Int] with F97[Int] with F98[Int] with F99[Int] with F100[Int]

case class InterfaceRecord128(
   f1: Int,  f2: Int,  f3: Int,  f4: Int,  f5: Int,  f6: Int,  f7: Int,  f8: Int,  f9: Int, f10: Int,
  f11: Int, f12: Int, f13: Int, f14: Int, f15: Int, f16: Int, f17: Int, f18: Int, f19: Int, f20: Int,
  f21: Int, f22: Int, f23: Int, f24: Int, f25: Int, f26: Int, f27: Int, f28: Int, f29: Int, f30: Int,
  f31: Int, f32: Int, f33: Int, f34: Int, f35: Int, f36: Int, f37: Int, f38: Int, f39: Int, f40: Int,
  f41: Int, f42: Int, f43: Int, f44: Int, f45: Int, f46: Int, f47: Int, f48: Int, f49: Int, f50: Int,
  f51: Int, f52: Int, f53: Int, f54: Int, f55: Int, f56: Int, f57: Int, f58: Int, f59: Int, f60: Int,
  f61: Int, f62: Int, f63: Int, f64: Int, f65: Int, f66: Int, f67: Int, f68: Int, f69: Int, f70: Int,
  f71: Int, f72: Int, f73: Int, f74: Int, f75: Int, f76: Int, f77: Int, f78: Int, f79: Int, f80: Int,
  f81: Int, f82: Int, f83: Int, f84: Int, f85: Int, f86: Int, f87: Int, f88: Int, f89: Int, f90: Int,
  f91: Int, f92: Int, f93: Int, f94: Int, f95: Int, f96: Int, f97: Int, f98: Int, f99: Int, f100: Int,
  f101: Int, f102: Int, f103: Int, f104: Int, f105: Int, f106: Int, f107: Int, f108: Int, f109: Int, f110: Int,
  f111: Int, f112: Int, f113: Int, f114: Int, f115: Int, f116: Int, f117: Int, f118: Int, f119: Int, f120: Int,
  f121: Int, f122: Int, f123: Int, f124: Int, f125: Int, f126: Int, f127: Int, f128: Int
) extends Record
  with   F1[Int] with   F2[Int] with   F3[Int] with   F4[Int] with   F5[Int] with   F6[Int] with   F7[Int] with   F8[Int] with   F9[Int] with  F10[Int]
  with  F11[Int] with  F12[Int] with  F13[Int] with  F14[Int] with  F15[Int] with  F16[Int] with  F17[Int] with  F18[Int] with  F19[Int] with  F20[Int]
  with  F21[Int] with  F22[Int] with  F23[Int] with  F24[Int] with  F25[Int] with  F26[Int] with  F27[Int] with  F28[Int] with  F29[Int] with  F30[Int]
  with  F31[Int] with  F32[Int] with  F33[Int] with  F34[Int] with  F35[Int] with  F36[Int] with  F37[Int] with  F38[Int] with  F39[Int] with  F40[Int]
  with  F41[Int] with  F42[Int] with  F43[Int] with  F44[Int] with  F45[Int] with  F46[Int] with  F47[Int] with  F48[Int] with  F49[Int] with  F50[Int]
  with  F51[Int] with  F52[Int] with  F53[Int] with  F54[Int] with  F55[Int] with  F56[Int] with  F57[Int] with  F58[Int] with  F59[Int] with  F60[Int]
  with  F61[Int] with  F62[Int] with  F63[Int] with  F64[Int] with  F65[Int] with  F66[Int] with  F67[Int] with  F68[Int] with  F69[Int] with  F70[Int]
  with  F71[Int] with  F72[Int] with  F73[Int] with  F74[Int] with  F75[Int] with  F76[Int] with  F77[Int] with  F78[Int] with  F79[Int] with  F80[Int]
  with  F81[Int] with  F82[Int] with  F83[Int] with  F84[Int] with  F85[Int] with  F86[Int] with  F87[Int] with  F88[Int] with  F89[Int] with  F90[Int]
  with  F91[Int] with  F92[Int] with  F93[Int] with  F94[Int] with  F95[Int] with  F96[Int] with  F97[Int] with  F98[Int] with  F99[Int] with F100[Int]
  with F101[Int] with F102[Int] with F103[Int] with F104[Int] with F105[Int] with F106[Int] with F107[Int] with F108[Int] with F109[Int] with F110[Int]
  with F111[Int] with F112[Int] with F113[Int] with F114[Int] with F115[Int] with F116[Int] with F117[Int] with F118[Int] with F119[Int] with F120[Int]
  with F121[Int] with F122[Int] with F123[Int] with F124[Int] with F125[Int] with F126[Int] with F127[Int] with F128[Int]

case class InterfaceRecord150(
   f1: Int,  f2: Int,  f3: Int,  f4: Int,  f5: Int,  f6: Int,  f7: Int,  f8: Int,  f9: Int, f10: Int,
  f11: Int, f12: Int, f13: Int, f14: Int, f15: Int, f16: Int, f17: Int, f18: Int, f19: Int, f20: Int,
  f21: Int, f22: Int, f23: Int, f24: Int, f25: Int, f26: Int, f27: Int, f28: Int, f29: Int, f30: Int,
  f31: Int, f32: Int, f33: Int, f34: Int, f35: Int, f36: Int, f37: Int, f38: Int, f39: Int, f40: Int,
  f41: Int, f42: Int, f43: Int, f44: Int, f45: Int, f46: Int, f47: Int, f48: Int, f49: Int, f50: Int,
  f51: Int, f52: Int, f53: Int, f54: Int, f55: Int, f56: Int, f57: Int, f58: Int, f59: Int, f60: Int,
  f61: Int, f62: Int, f63: Int, f64: Int, f65: Int, f66: Int, f67: Int, f68: Int, f69: Int, f70: Int,
  f71: Int, f72: Int, f73: Int, f74: Int, f75: Int, f76: Int, f77: Int, f78: Int, f79: Int, f80: Int,
  f81: Int, f82: Int, f83: Int, f84: Int, f85: Int, f86: Int, f87: Int, f88: Int, f89: Int, f90: Int,
  f91: Int, f92: Int, f93: Int, f94: Int, f95: Int, f96: Int, f97: Int, f98: Int, f99: Int, f100: Int,
  f101: Int, f102: Int, f103: Int, f104: Int, f105: Int, f106: Int, f107: Int, f108: Int, f109: Int, f110: Int,
  f111: Int, f112: Int, f113: Int, f114: Int, f115: Int, f116: Int, f117: Int, f118: Int, f119: Int, f120: Int,
  f121: Int, f122: Int, f123: Int, f124: Int, f125: Int, f126: Int, f127: Int, f128: Int, f129: Int, f130: Int,
  f131: Int, f132: Int, f133: Int, f134: Int, f135: Int, f136: Int, f137: Int, f138: Int, f139: Int, f140: Int,
  f141: Int, f142: Int, f143: Int, f144: Int, f145: Int, f146: Int, f147: Int, f148: Int, f149: Int, f150: Int
) extends Record
  with   F1[Int] with   F2[Int] with   F3[Int] with   F4[Int] with   F5[Int] with   F6[Int] with   F7[Int] with   F8[Int] with   F9[Int] with  F10[Int]
  with  F11[Int] with  F12[Int] with  F13[Int] with  F14[Int] with  F15[Int] with  F16[Int] with  F17[Int] with  F18[Int] with  F19[Int] with  F20[Int]
  with  F21[Int] with  F22[Int] with  F23[Int] with  F24[Int] with  F25[Int] with  F26[Int] with  F27[Int] with  F28[Int] with  F29[Int] with  F30[Int]
  with  F31[Int] with  F32[Int] with  F33[Int] with  F34[Int] with  F35[Int] with  F36[Int] with  F37[Int] with  F38[Int] with  F39[Int] with  F40[Int]
  with  F41[Int] with  F42[Int] with  F43[Int] with  F44[Int] with  F45[Int] with  F46[Int] with  F47[Int] with  F48[Int] with  F49[Int] with  F50[Int]
  with  F51[Int] with  F52[Int] with  F53[Int] with  F54[Int] with  F55[Int] with  F56[Int] with  F57[Int] with  F58[Int] with  F59[Int] with  F60[Int]
  with  F61[Int] with  F62[Int] with  F63[Int] with  F64[Int] with  F65[Int] with  F66[Int] with  F67[Int] with  F68[Int] with  F69[Int] with  F70[Int]
  with  F71[Int] with  F72[Int] with  F73[Int] with  F74[Int] with  F75[Int] with  F76[Int] with  F77[Int] with  F78[Int] with  F79[Int] with  F80[Int]
  with  F81[Int] with  F82[Int] with  F83[Int] with  F84[Int] with  F85[Int] with  F86[Int] with  F87[Int] with  F88[Int] with  F89[Int] with  F90[Int]
  with  F91[Int] with  F92[Int] with  F93[Int] with  F94[Int] with  F95[Int] with  F96[Int] with  F97[Int] with  F98[Int] with  F99[Int] with F100[Int]
  with F101[Int] with F102[Int] with F103[Int] with F104[Int] with F105[Int] with F106[Int] with F107[Int] with F108[Int] with F109[Int] with F110[Int]
  with F111[Int] with F112[Int] with F113[Int] with F114[Int] with F115[Int] with F116[Int] with F117[Int] with F118[Int] with F119[Int] with F120[Int]
  with F121[Int] with F122[Int] with F123[Int] with F124[Int] with F125[Int] with F126[Int] with F127[Int] with F128[Int] with F129[Int] with F130[Int]
  with F131[Int] with F132[Int] with F133[Int] with F134[Int] with F135[Int] with F136[Int] with F137[Int] with F138[Int] with F139[Int] with F140[Int]
  with F141[Int] with F142[Int] with F143[Int] with F144[Int] with F145[Int] with F146[Int] with F147[Int] with F148[Int] with F149[Int] with F150[Int]

case class InterfaceRecord200(
   f1: Int,  f2: Int,  f3: Int,  f4: Int,  f5: Int,  f6: Int,  f7: Int,  f8: Int,  f9: Int, f10: Int,
  f11: Int, f12: Int, f13: Int, f14: Int, f15: Int, f16: Int, f17: Int, f18: Int, f19: Int, f20: Int,
  f21: Int, f22: Int, f23: Int, f24: Int, f25: Int, f26: Int, f27: Int, f28: Int, f29: Int, f30: Int,
  f31: Int, f32: Int, f33: Int, f34: Int, f35: Int, f36: Int, f37: Int, f38: Int, f39: Int, f40: Int,
  f41: Int, f42: Int, f43: Int, f44: Int, f45: Int, f46: Int, f47: Int, f48: Int, f49: Int, f50: Int,
  f51: Int, f52: Int, f53: Int, f54: Int, f55: Int, f56: Int, f57: Int, f58: Int, f59: Int, f60: Int,
  f61: Int, f62: Int, f63: Int, f64: Int, f65: Int, f66: Int, f67: Int, f68: Int, f69: Int, f70: Int,
  f71: Int, f72: Int, f73: Int, f74: Int, f75: Int, f76: Int, f77: Int, f78: Int, f79: Int, f80: Int,
  f81: Int, f82: Int, f83: Int, f84: Int, f85: Int, f86: Int, f87: Int, f88: Int, f89: Int, f90: Int,
  f91: Int, f92: Int, f93: Int, f94: Int, f95: Int, f96: Int, f97: Int, f98: Int, f99: Int, f100: Int,
  f101: Int, f102: Int, f103: Int, f104: Int, f105: Int, f106: Int, f107: Int, f108: Int, f109: Int, f110: Int,
  f111: Int, f112: Int, f113: Int, f114: Int, f115: Int, f116: Int, f117: Int, f118: Int, f119: Int, f120: Int,
  f121: Int, f122: Int, f123: Int, f124: Int, f125: Int, f126: Int, f127: Int, f128: Int, f129: Int, f130: Int,
  f131: Int, f132: Int, f133: Int, f134: Int, f135: Int, f136: Int, f137: Int, f138: Int, f139: Int, f140: Int,
  f141: Int, f142: Int, f143: Int, f144: Int, f145: Int, f146: Int, f147: Int, f148: Int, f149: Int, f150: Int,
  f151: Int, f152: Int, f153: Int, f154: Int, f155: Int, f156: Int, f157: Int, f158: Int, f159: Int, f160: Int,
  f161: Int, f162: Int, f163: Int, f164: Int, f165: Int, f166: Int, f167: Int, f168: Int, f169: Int, f170: Int,
  f171: Int, f172: Int, f173: Int, f174: Int, f175: Int, f176: Int, f177: Int, f178: Int, f179: Int, f180: Int,
  f181: Int, f182: Int, f183: Int, f184: Int, f185: Int, f186: Int, f187: Int, f188: Int, f189: Int, f190: Int,
  f191: Int, f192: Int, f193: Int, f194: Int, f195: Int, f196: Int, f197: Int, f198: Int, f199: Int, f200: Int
) extends Record
  with   F1[Int] with   F2[Int] with   F3[Int] with   F4[Int] with   F5[Int] with   F6[Int] with   F7[Int] with   F8[Int] with   F9[Int] with  F10[Int]
  with  F11[Int] with  F12[Int] with  F13[Int] with  F14[Int] with  F15[Int] with  F16[Int] with  F17[Int] with  F18[Int] with  F19[Int] with  F20[Int]
  with  F21[Int] with  F22[Int] with  F23[Int] with  F24[Int] with  F25[Int] with  F26[Int] with  F27[Int] with  F28[Int] with  F29[Int] with  F30[Int]
  with  F31[Int] with  F32[Int] with  F33[Int] with  F34[Int] with  F35[Int] with  F36[Int] with  F37[Int] with  F38[Int] with  F39[Int] with  F40[Int]
  with  F41[Int] with  F42[Int] with  F43[Int] with  F44[Int] with  F45[Int] with  F46[Int] with  F47[Int] with  F48[Int] with  F49[Int] with  F50[Int]
  with  F51[Int] with  F52[Int] with  F53[Int] with  F54[Int] with  F55[Int] with  F56[Int] with  F57[Int] with  F58[Int] with  F59[Int] with  F60[Int]
  with  F61[Int] with  F62[Int] with  F63[Int] with  F64[Int] with  F65[Int] with  F66[Int] with  F67[Int] with  F68[Int] with  F69[Int] with  F70[Int]
  with  F71[Int] with  F72[Int] with  F73[Int] with  F74[Int] with  F75[Int] with  F76[Int] with  F77[Int] with  F78[Int] with  F79[Int] with  F80[Int]
  with  F81[Int] with  F82[Int] with  F83[Int] with  F84[Int] with  F85[Int] with  F86[Int] with  F87[Int] with  F88[Int] with  F89[Int] with  F90[Int]
  with  F91[Int] with  F92[Int] with  F93[Int] with  F94[Int] with  F95[Int] with  F96[Int] with  F97[Int] with  F98[Int] with  F99[Int] with F100[Int]
  with F101[Int] with F102[Int] with F103[Int] with F104[Int] with F105[Int] with F106[Int] with F107[Int] with F108[Int] with F109[Int] with F110[Int]
  with F111[Int] with F112[Int] with F113[Int] with F114[Int] with F115[Int] with F116[Int] with F117[Int] with F118[Int] with F119[Int] with F120[Int]
  with F121[Int] with F122[Int] with F123[Int] with F124[Int] with F125[Int] with F126[Int] with F127[Int] with F128[Int] with F129[Int] with F130[Int]
  with F131[Int] with F132[Int] with F133[Int] with F134[Int] with F135[Int] with F136[Int] with F137[Int] with F138[Int] with F139[Int] with F140[Int]
  with F141[Int] with F142[Int] with F143[Int] with F144[Int] with F145[Int] with F146[Int] with F147[Int] with F148[Int] with F149[Int] with F150[Int]
  with F151[Int] with F152[Int] with F153[Int] with F154[Int] with F155[Int] with F156[Int] with F157[Int] with F158[Int] with F159[Int] with F160[Int]
  with F161[Int] with F162[Int] with F163[Int] with F164[Int] with F165[Int] with F166[Int] with F167[Int] with F168[Int] with F169[Int] with F170[Int]
  with F171[Int] with F172[Int] with F173[Int] with F174[Int] with F175[Int] with F176[Int] with F177[Int] with F178[Int] with F179[Int] with F180[Int]
  with F181[Int] with F182[Int] with F183[Int] with F184[Int] with F185[Int] with F186[Int] with F187[Int] with F188[Int] with F189[Int] with F190[Int]
  with F191[Int] with F192[Int] with F193[Int] with F194[Int] with F195[Int] with F196[Int] with F197[Int] with F198[Int] with F199[Int] with F200[Int]

case class InterfaceRecord250(
   f1: Int,  f2: Int,  f3: Int,  f4: Int,  f5: Int,  f6: Int,  f7: Int,  f8: Int,  f9: Int, f10: Int,
  f11: Int, f12: Int, f13: Int, f14: Int, f15: Int, f16: Int, f17: Int, f18: Int, f19: Int, f20: Int,
  f21: Int, f22: Int, f23: Int, f24: Int, f25: Int, f26: Int, f27: Int, f28: Int, f29: Int, f30: Int,
  f31: Int, f32: Int, f33: Int, f34: Int, f35: Int, f36: Int, f37: Int, f38: Int, f39: Int, f40: Int,
  f41: Int, f42: Int, f43: Int, f44: Int, f45: Int, f46: Int, f47: Int, f48: Int, f49: Int, f50: Int,
  f51: Int, f52: Int, f53: Int, f54: Int, f55: Int, f56: Int, f57: Int, f58: Int, f59: Int, f60: Int,
  f61: Int, f62: Int, f63: Int, f64: Int, f65: Int, f66: Int, f67: Int, f68: Int, f69: Int, f70: Int,
  f71: Int, f72: Int, f73: Int, f74: Int, f75: Int, f76: Int, f77: Int, f78: Int, f79: Int, f80: Int,
  f81: Int, f82: Int, f83: Int, f84: Int, f85: Int, f86: Int, f87: Int, f88: Int, f89: Int, f90: Int,
  f91: Int, f92: Int, f93: Int, f94: Int, f95: Int, f96: Int, f97: Int, f98: Int, f99: Int, f100: Int,
  f101: Int, f102: Int, f103: Int, f104: Int, f105: Int, f106: Int, f107: Int, f108: Int, f109: Int, f110: Int,
  f111: Int, f112: Int, f113: Int, f114: Int, f115: Int, f116: Int, f117: Int, f118: Int, f119: Int, f120: Int,
  f121: Int, f122: Int, f123: Int, f124: Int, f125: Int, f126: Int, f127: Int, f128: Int, f129: Int, f130: Int,
  f131: Int, f132: Int, f133: Int, f134: Int, f135: Int, f136: Int, f137: Int, f138: Int, f139: Int, f140: Int,
  f141: Int, f142: Int, f143: Int, f144: Int, f145: Int, f146: Int, f147: Int, f148: Int, f149: Int, f150: Int,
  f151: Int, f152: Int, f153: Int, f154: Int, f155: Int, f156: Int, f157: Int, f158: Int, f159: Int, f160: Int,
  f161: Int, f162: Int, f163: Int, f164: Int, f165: Int, f166: Int, f167: Int, f168: Int, f169: Int, f170: Int,
  f171: Int, f172: Int, f173: Int, f174: Int, f175: Int, f176: Int, f177: Int, f178: Int, f179: Int, f180: Int,
  f181: Int, f182: Int, f183: Int, f184: Int, f185: Int, f186: Int, f187: Int, f188: Int, f189: Int, f190: Int,
  f191: Int, f192: Int, f193: Int, f194: Int, f195: Int, f196: Int, f197: Int, f198: Int, f199: Int, f200: Int,
  f201: Int, f202: Int, f203: Int, f204: Int, f205: Int, f206: Int, f207: Int, f208: Int, f209: Int, f210: Int,
  f211: Int, f212: Int, f213: Int, f214: Int, f215: Int, f216: Int, f217: Int, f218: Int, f219: Int, f220: Int,
  f221: Int, f222: Int, f223: Int, f224: Int, f225: Int, f226: Int, f227: Int, f228: Int, f229: Int, f230: Int,
  f231: Int, f232: Int, f233: Int, f234: Int, f235: Int, f236: Int, f237: Int, f238: Int, f239: Int, f240: Int,
  f241: Int, f242: Int, f243: Int, f244: Int, f245: Int, f246: Int, f247: Int, f248: Int, f249: Int, f250: Int
) extends Record
  with   F1[Int] with   F2[Int] with   F3[Int] with   F4[Int] with   F5[Int] with   F6[Int] with   F7[Int] with   F8[Int] with   F9[Int] with  F10[Int]
  with  F11[Int] with  F12[Int] with  F13[Int] with  F14[Int] with  F15[Int] with  F16[Int] with  F17[Int] with  F18[Int] with  F19[Int] with  F20[Int]
  with  F21[Int] with  F22[Int] with  F23[Int] with  F24[Int] with  F25[Int] with  F26[Int] with  F27[Int] with  F28[Int] with  F29[Int] with  F30[Int]
  with  F31[Int] with  F32[Int] with  F33[Int] with  F34[Int] with  F35[Int] with  F36[Int] with  F37[Int] with  F38[Int] with  F39[Int] with  F40[Int]
  with  F41[Int] with  F42[Int] with  F43[Int] with  F44[Int] with  F45[Int] with  F46[Int] with  F47[Int] with  F48[Int] with  F49[Int] with  F50[Int]
  with  F51[Int] with  F52[Int] with  F53[Int] with  F54[Int] with  F55[Int] with  F56[Int] with  F57[Int] with  F58[Int] with  F59[Int] with  F60[Int]
  with  F61[Int] with  F62[Int] with  F63[Int] with  F64[Int] with  F65[Int] with  F66[Int] with  F67[Int] with  F68[Int] with  F69[Int] with  F70[Int]
  with  F71[Int] with  F72[Int] with  F73[Int] with  F74[Int] with  F75[Int] with  F76[Int] with  F77[Int] with  F78[Int] with  F79[Int] with  F80[Int]
  with  F81[Int] with  F82[Int] with  F83[Int] with  F84[Int] with  F85[Int] with  F86[Int] with  F87[Int] with  F88[Int] with  F89[Int] with  F90[Int]
  with  F91[Int] with  F92[Int] with  F93[Int] with  F94[Int] with  F95[Int] with  F96[Int] with  F97[Int] with  F98[Int] with  F99[Int] with F100[Int]
  with F101[Int] with F102[Int] with F103[Int] with F104[Int] with F105[Int] with F106[Int] with F107[Int] with F108[Int] with F109[Int] with F110[Int]
  with F111[Int] with F112[Int] with F113[Int] with F114[Int] with F115[Int] with F116[Int] with F117[Int] with F118[Int] with F119[Int] with F120[Int]
  with F121[Int] with F122[Int] with F123[Int] with F124[Int] with F125[Int] with F126[Int] with F127[Int] with F128[Int] with F129[Int] with F130[Int]
  with F131[Int] with F132[Int] with F133[Int] with F134[Int] with F135[Int] with F136[Int] with F137[Int] with F138[Int] with F139[Int] with F140[Int]
  with F141[Int] with F142[Int] with F143[Int] with F144[Int] with F145[Int] with F146[Int] with F147[Int] with F148[Int] with F149[Int] with F150[Int]
  with F151[Int] with F152[Int] with F153[Int] with F154[Int] with F155[Int] with F156[Int] with F157[Int] with F158[Int] with F159[Int] with F160[Int]
  with F161[Int] with F162[Int] with F163[Int] with F164[Int] with F165[Int] with F166[Int] with F167[Int] with F168[Int] with F169[Int] with F170[Int]
  with F171[Int] with F172[Int] with F173[Int] with F174[Int] with F175[Int] with F176[Int] with F177[Int] with F178[Int] with F179[Int] with F180[Int]
  with F181[Int] with F182[Int] with F183[Int] with F184[Int] with F185[Int] with F186[Int] with F187[Int] with F188[Int] with F189[Int] with F190[Int]
  with F191[Int] with F192[Int] with F193[Int] with F194[Int] with F195[Int] with F196[Int] with F197[Int] with F198[Int] with F199[Int] with F200[Int]
  with F201[Int] with F202[Int] with F203[Int] with F204[Int] with F205[Int] with F206[Int] with F207[Int] with F208[Int] with F209[Int] with F210[Int]
  with F211[Int] with F212[Int] with F213[Int] with F214[Int] with F215[Int] with F216[Int] with F217[Int] with F218[Int] with F219[Int] with F220[Int]
  with F221[Int] with F222[Int] with F223[Int] with F224[Int] with F225[Int] with F226[Int] with F227[Int] with F228[Int] with F229[Int] with F230[Int]
  with F231[Int] with F232[Int] with F233[Int] with F234[Int] with F235[Int] with F236[Int] with F237[Int] with F238[Int] with F239[Int] with F240[Int]
  with F241[Int] with F242[Int] with F243[Int] with F244[Int] with F245[Int] with F246[Int] with F247[Int] with F248[Int] with F249[Int] with F250[Int]

