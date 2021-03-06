SUMMARY = "Small library that defines common error values for all GnuPG components"
HOMEPAGE = "http://www.gnupg.org/related_software/libgpg-error/"
BUGTRACKER = "https://bugs.g10code.com/gnupg/index"

LICENSE = "GPLv2+ & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
                    file://COPYING.LIB;md5=2d5025d4aa3495befef8f17206a5b0a1 \
                    file://src/gpg-error.h.in;endline=23;md5=64af9846baaf852793fd3a5af393acbf \
                    file://src/init.c;endline=20;md5=872b2389fe9bae7ffb80d2b91225afbc"


SECTION = "libs"

UPSTREAM_CHECK_URI = "https://gnupg.org/download/index.html"
SRC_URI = "${GNUPG_MIRROR}/libgpg-error/libgpg-error-${PV}.tar.bz2 \
           file://pkgconfig.patch \
	  "

SRC_URI[md5sum] = "d9fa545922a5060cbfbd87464bc31686"
SRC_URI[sha256sum] = "f628f75843433b38b05af248121beb7db5bd54bb2106f384edac39934261320c"

BINCONFIG = "${bindir}/gpg-error-config"

inherit autotools binconfig-disabled pkgconfig gettext
CPPFLAGS += "-P"
do_compile_prepend() {
	TARGET_FILE=linux-gnu
	if [ ${TARGET_OS} != "linux" ]; then
		TARGET_FILE=${TARGET_OS}
	fi

	case ${TARGET_ARCH} in
	  aarch64_be) TUPLE=aarch64-unknown-linux-gnu ;;
	  arm)	      TUPLE=arm-unknown-linux-gnueabi ;;
	  armeb)      TUPLE=arm-unknown-linux-gnueabi ;;
	  i586|i686)  TUPLE=i686-pc-linux-gnu ;;
	  mips*el)    TUPLE=mipsel-unknown-linux-gnu ;;
	  mips*)      TUPLE=mips-unknown-linux-gnu ;;
	  x86_64)     TUPLE=x86_64-pc-linux-gnu ;;
	  *)          TUPLE=${TARGET_ARCH}-unknown-linux-gnu ;; 
	esac

	cp ${S}/src/syscfg/lock-obj-pub.$TUPLE.h \
	  ${S}/src/syscfg/lock-obj-pub.$TARGET_FILE.h
}

do_install_append() {
	# we don't have common lisp in OE
	rm -rf "${D}${datadir}/common-lisp/"
}

FILES_${PN}-dev += "${bindir}/gpg-error"

BBCLASSEXTEND = "native"
